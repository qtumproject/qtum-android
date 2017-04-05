package org.qtum.mromanovsky.qtum.dataprovider;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.google.gson.Gson;

import org.bitcoinj.wallet.Wallet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UpdateService extends Service {

    public final int DEFAULT_NOTIFICATION_ID = 101;
    private NotificationManager notificationManager;
    private TransactionListener mTransactionListener = null;
    private BalanceChangeListener mBalanceChangeListener;
    private Notification notification;
    private Socket socket;
    private int totalTransaction = 0;

    UpdateBinder mUpdateBinder = new UpdateBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            socket = IO.socket("http://163.172.68.103:5931/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONArray arr = new JSONArray();
                for(String address : KeyStorage.getInstance().getAddresses()){
                    arr.put(address);
                }
                socket.emit("subscribe","balance_subscribe", arr);
                sendNotification("Default","Default","Default",null);

            }
        }).on("balance_changed", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(mBalanceChangeListener !=null) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        double unconfirmedBalance = data.getDouble("unconfirmedBalance") / 100000000;
                        double balance = data.getDouble("balance") / 100000000;
                        mBalanceChangeListener.onChangeBalance(String.valueOf(balance), String.valueOf(unconfirmedBalance));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).on("new_transaction", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new Gson();
                JSONObject data = (JSONObject) args[0];
                History history = gson.fromJson(data.toString(),History.class);
                if(mTransactionListener != null) {
                    mTransactionListener.onNewHistory(history);
                    if(!mTransactionListener.getVisibility()){
                        if(history.getBlockTime()!=null) {
                            totalTransaction++;
                            sendNotification("New confirmed transaction", totalTransaction + " new confirmed transaction",
                                    "Touch to open transaction history", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        }
                    }
                } else {
                    if(history.getBlockTime()!=null) {
                        totalTransaction++;
                        sendNotification("New confirmed transaction", totalTransaction + " new confirmed transaction",
                                "Touch to open transaction history", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    }
                }

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });


        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(DEFAULT_NOTIFICATION_ID);
        socket.emit("unsubscribe","quantumd/addressbalance");
        socket.disconnect();
        stopSelf();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        loadWalletFromFile(new LoadWalletFromFileCallBack() {
            @Override
            public void onSuccess() {
                socket.connect();
            }
        });

        return START_REDELIVER_INTENT;
    }

    private void loadWalletFromFile(final LoadWalletFromFileCallBack callback) {
        KeyStorage.getInstance().loadWalletFromFile(getBaseContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Wallet>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Wallet wallet) {
                        callback.onSuccess();
                    }
                });
    }

    interface LoadWalletFromFileCallBack {
        void onSuccess();
    }

    public void sendNotification(String Ticker,String Title,String Text, Uri sound) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        notificationIntent.putExtra("notification_action", true);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(contentIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setTicker(Ticker)
                .setContentTitle(Title)
                .setContentText(Text)
                .setWhen(System.currentTimeMillis())
                .setSound(sound);

        if (android.os.Build.VERSION.SDK_INT<=15) {
            notification = builder.getNotification();
        }else{
            notification = builder.build();
        }

        startForeground(DEFAULT_NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mUpdateBinder;
    }

    public void addTransactionListener(TransactionListener updateServiceListener){
        mTransactionListener = updateServiceListener;
    }

    public void removeTransactionListener(){
        mTransactionListener = null;
    }

    public void addBalanceChangeListener(BalanceChangeListener balanceChangeListener){
        mBalanceChangeListener = balanceChangeListener;
    }

    public void removeBalanceChangeListener(){
        mBalanceChangeListener = null;
    }

    public void clearNotification(){
        if(totalTransaction!=0) {
            sendNotification("Default", "Default", "Default", null);
            totalTransaction = 0;
        }
    }

    public class UpdateBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}