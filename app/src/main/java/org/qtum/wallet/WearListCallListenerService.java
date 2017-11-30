package org.qtum.wallet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.WatchUpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kirillvolkov on 21.11.2017.
 */

public class WearListCallListenerService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener {

    public static final String ITEMS = "items";
    public static final String BALANCE = "balance";
    public static final String UNC_BALANCE = "uncBalance";
    public static final String ADDRESS = "address";
    public static final String CURR_TIME_MILLS = "CURR_TIME_MILLS";
    private static final String TAG = "WEAR SERVICE";

    private GoogleApiClient mApiClient;
    ServiceConnection mServiceConnection;
    WatchUpdateService mUpdateService;
    List<String> addresses;
    String currentAddress;

    @Override
    public void onCreate() {
        super.onCreate();
        initGoogleApiClient();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if(messageEvent.getPath().contains("/get_history")) {
            addresses = getPublicAddresses();
            currentAddress = QtumSharedPreference.getInstance().getCurrentAddress(getApplicationContext());
            if (QtumApplication.instance == null || QtumApplication.instance.getWearableMessagingProvider() == null) {
                upSocketService();
            } else {
                sendData();
            }
        } else {
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        if(mServiceConnection != null) {
            unbindService(mServiceConnection);
        }

        super.onDestroy();
        if (mApiClient != null) {
            mApiClient.disconnect();
        }
    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.MessageApi.addListener(mApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private List<String> getPublicAddresses() {
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        return tinyDB.getPublicAddresses();
    }

    private void upSocketService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder instanceof WatchUpdateService.WatchUpdateBinder) {
                    mUpdateService = ((WatchUpdateService.WatchUpdateBinder) iBinder).getService();
                    mUpdateService.clearNotification();
                    mUpdateService.initGoogleApiClient(addresses, currentAddress);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        Intent intent = new Intent(this, WatchUpdateService.class);
        startForegroundService(intent);

        bindService(new Intent(this,
                WatchUpdateService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
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

    private void sendData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<History> histories = new ArrayList<>();
                histories = QtumApplication.instance.getWearableMessagingProvider().getOperations();
                String balance = QtumApplication.instance.getWearableMessagingProvider().getBalance();
                String uncBalance = QtumApplication.instance.getWearableMessagingProvider().getUnconfirmedBalance();
                String address = QtumApplication.instance.getWearableMessagingProvider().getAddress();
                Gson gson = new Gson();
                String s = gson.toJson(histories);
                sendData(s, balance, uncBalance, address);
            }

        }).start();
    }
}
