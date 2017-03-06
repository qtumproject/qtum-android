package org.qtum.mromanovsky.qtum.dataprovider;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.bitcoinj.wallet.Wallet;
import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class UpdateService extends Service {

    private final String TAG = "UpdateService";
    public static final int DEFAULT_NOTIFICATION_ID = 101;
    private NotificationManager notificationManager;
    Subscription subscription;
    boolean monitoringFlag = false;
    UpdateData mUpdateData = null;
    int currentCount;
    Notification notification;
    Uri uri;

    UpdateBinder mUpdateBinder = new UpdateBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(DEFAULT_NOTIFICATION_ID);
        stopSelf();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        loadWalletFromFile(new LoadWalletFromFileCallBack() {
            @Override
            public void onSuccess() {
                startMonitoringHistory();
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
                        if(mUpdateData!=null) {
                            mUpdateData.updateDate();
                        }
                        callback.onSuccess();
                    }
                });
    }

    public void startMonitoringHistory() {
        monitoringFlag = true;
        //sendDefaultNotification();
        currentCount = QtumSharedPreference.getInstance().getHistoryCount(getBaseContext());
        subscription = Observable.interval(1000, 3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    public void call(Long aLong) {
                        QtumService.newInstance().getHistoryCount(KeyStorage.getInstance().getAddresses(), new QtumService.GetHistoryCountCallBack() {
                            @Override
                            public void onResponse(int count) {
                                Log.d("hello", ""+count);
                                if(count!=currentCount){
                                    sendNotification("Ticker","New Transaction","Count: " + (count-currentCount));
                                    subscription.unsubscribe();
                                }
                            }
                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
    }

    public void unsubscribe(){
        monitoringFlag = false;
        subscription.unsubscribe();
    }

    //TODO: merge with sendNotification method
    public void sendDefaultNotification(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        notificationIntent.putExtra("notification_action", true);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(contentIntent)
                .setOngoing(true)   //Can't be swiped out
                .setSmallIcon(R.drawable.ic_launcher)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))   // большая картинка
                .setTicker("Default Ticker")
                .setContentTitle("Default Title") //Заголовок
                .setContentText("Default Text") // Текст уведомления
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT<=15) {
            notification = builder.getNotification(); // API-15 and lower
        }else{
            notification = builder.build();
        }

        startForeground(DEFAULT_NOTIFICATION_ID, notification);
    }

    public boolean isMonitoring(){
        return monitoringFlag;
    }

    public interface LoadWalletFromFileCallBack {
        void onSuccess();
    }

    public void sendNotification(String Ticker,String Title,String Text) {

        //These three lines makes Notification to open main activity after clicking on it
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        notificationIntent.putExtra("notification_action", true);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(contentIntent)
                .setOngoing(true)   //Can't be swiped out
                .setSmallIcon(R.drawable.ic_launcher)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))   // большая картинка
                .setTicker(Ticker)
                .setContentTitle(Title) //Заголовок
                .setContentText(Text) // Текст уведомления
                .setWhen(System.currentTimeMillis())
                .setSound(uri);

        if (android.os.Build.VERSION.SDK_INT<=15) {
            notification = builder.getNotification(); // API-15 and lower
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

    public void registerListener(UpdateData updateData){
        mUpdateData = updateData;
    }


    public class UpdateBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}