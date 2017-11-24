package org.qtum.wallet.dataprovider.services.update_service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qtum.wallet.dataprovider.firebase.FirebaseSharedPreferences;
import org.qtum.wallet.dataprovider.firebase.listeners.FireBaseTokenRefreshListener;
import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.utils.CurrentNetParams;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static org.qtum.wallet.WearListCallListenerService.ADDRESS;
import static org.qtum.wallet.WearListCallListenerService.BALANCE;
import static org.qtum.wallet.WearListCallListenerService.CURR_TIME_MILLS;
import static org.qtum.wallet.WearListCallListenerService.ITEMS;
import static org.qtum.wallet.WearListCallListenerService.UNC_BALANCE;

/**
 * Created by kirillvolkov on 23.11.2017.
 */

public class WatchUpdateService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener {

    private static final String TAG = "WATCH_UPDATE_SERVICE";
    private WatchUpdateService.WatchUpdateBinder mUpdateBinder = new WatchUpdateService.WatchUpdateBinder();

    private Socket socket;
    private boolean monitoringFlag;
    private JSONArray mAddresses;

    String[] firebaseTokens;

    private String mFirebasePrevToken;
    private String mFirebaseCurrentToken;

    private BigDecimal unconfirmedBalance;
    private BigDecimal balance;

    private GoogleApiClient mApiClient;

    private List<String> addresses;
    private String currentAddress;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mUpdateBinder;
    }

    @Override
    public void onDestroy() {
        stopMonitoring();
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.MessageApi.addListener(mApiClient, this);

        try {
            SSLContext mySSLContext = SSLContext.getInstance("TLS");
            HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}
                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {}
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};
            mySSLContext.init(null, trustAllCerts, null);
            IO.Options opts = new IO.Options();
            opts.sslContext = mySSLContext;
            opts.hostnameVerifier = myHostnameVerifier;

            socket = IO.socket(CurrentNetParams.getUrl(), opts);
            Log.d(TAG, "onCreate: " + CurrentNetParams.getUrl());
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                subscribeSocket();
            }
        }).on("balance_changed", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        unconfirmedBalance = (new BigDecimal(data.getString("unconfirmedBalance"))).divide(new BigDecimal("100000000"), MathContext.DECIMAL128);
                        balance = (new BigDecimal(data.getString("balance"))).divide(new BigDecimal("100000000"), MathContext.DECIMAL128);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onBalanceChange();
                } catch (ClassCastException e) {
                }
            }
        }).on("new_transaction", new Emitter.Listener() {
            @Override
            public void call(Object... args) {}
        }).on("token_balance_change", new Emitter.Listener() {
            @Override
            public void call(Object... args) {}
        }).on("contract_purchase", new Emitter.Listener() {
            @Override
            public void call(Object... args) {}
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("socket", "disconnect");
            }
        });
        startMonitoring();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        stopForeground(true);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().contains("/stop_service")){
            stopForeground(true);
        }
    }

    public class WatchUpdateBinder extends Binder {
        public WatchUpdateService getService() {
            return WatchUpdateService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new android.support.v4.app.NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }

        firebaseTokens = FirebaseSharedPreferences.getInstance().getFirebaseTokens(getApplicationContext());
        mFirebasePrevToken = firebaseTokens[0];
        mFirebaseCurrentToken = firebaseTokens[1];

        FirebaseSharedPreferences.getInstance().addFirebaseTokenRefreshListener(new FireBaseTokenRefreshListener() {
            @Override
            public void onRefresh(String prevToken, String currentToken) {
                mFirebasePrevToken = prevToken;
                mFirebaseCurrentToken = currentToken;
                subscribeSocket();
            }
        });
    }

    private void onBalanceChange() {
        QtumService.newInstance().getHistoryListForSeveralAddresses(addresses, 10, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(HistoryResponse historyResponse) {
                        Gson gson = new Gson();
                        List<History> items = historyResponse.getItems();
                        for (History item : items) {
                            calculateChangeInBalance(item, addresses);
                        }
                        String s = gson.toJson(items);
                        Log.d(TAG, "onNext: HISTORY ITEMS = " + s);
                        sendData(s, balance.toString(), unconfirmedBalance.toString(), currentAddress);
                    }
                });
    }

    private void sendData(String items, String balance, String uncBalance, String address) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data");
        putDataMapReq.getDataMap().putLong(CURR_TIME_MILLS, System.currentTimeMillis());
        putDataMapReq.getDataMap().putString(ITEMS, items);
        putDataMapReq.getDataMap().putString(BALANCE, balance);
        putDataMapReq.getDataMap().putString(UNC_BALANCE, uncBalance);
        putDataMapReq.getDataMap().putString(ADDRESS, address);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
        DataApi.DataItemResult await = Wearable.DataApi.putDataItem(mApiClient, putDataReq).await();
    }

    public void initGoogleApiClient(List<String> addresses, String currentAdddress) {
        this.addresses = addresses;
        this.currentAddress = currentAdddress;
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
    }

    public void startMonitoring() {
        if (!monitoringFlag) {
            mAddresses = new JSONArray();
            for (String address : addresses) {
                mAddresses.put(address);
            }
            socket.connect();
            monitoringFlag = true;
        }
    }

    public void stopMonitoring() {
        if(socket != null) {
            mApiClient.disconnect();
            JSONObject obj = new JSONObject();
            try {
                obj.put("notificationToken", mFirebaseCurrentToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("unsubscribe", "balance_subscribe", null, obj);
            socket.disconnect();
        }
            monitoringFlag = false;
            mAddresses = null;
    }

    public void clearNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    private void subscribeSocket() {
        subscribeBalanceChange(mFirebasePrevToken, mFirebaseCurrentToken);
    }

    private void subscribeBalanceChange(String prevToken, String currentToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("notificationToken", currentToken);
            jsonObject.put("prevToken", prevToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("subscribe", "balance_subscribe", mAddresses, jsonObject);
    }

    private void calculateChangeInBalance(History history, List<String> addresses) {
        BigDecimal changeInBalance = calculateVout(history, addresses).subtract(calculateVin(history, addresses));
        history.setChangeInBalance(changeInBalance);
    }

    private BigDecimal calculateVin(History history, List<String> addresses) {
        BigDecimal totalVin = new BigDecimal("0.0");
        boolean equals = false;
        for (Vin vin : history.getVin()) {
            for (String address : addresses) {
                if (vin.getAddress().equals(address)) {
                    vin.setOwnAddress(true);
                    equals = true;
                }
            }
        }
        if (equals) {
            totalVin = history.getAmount();
        }
        return totalVin;
    }

    private BigDecimal calculateVout(History history, List<String> addresses) {
        BigDecimal totalVout = new BigDecimal("0.0");
        for (Vout vout : history.getVout()) {
            for (String address : addresses) {
                if (vout.getAddress().equals(address)) {
                    vout.setOwnAddress(true);
                    totalVout = totalVout.add(vout.getValue());
                }
            }
        }
        return totalVout;
    }
}
