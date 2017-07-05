package com.pixelplex.qtum.dataprovider;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.bitcoinj.wallet.Wallet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.listeners.BalanceChangeListener;
import com.pixelplex.qtum.dataprovider.listeners.FireBaseTokenRefreshListener;
import com.pixelplex.qtum.dataprovider.listeners.TokenListener;
import com.pixelplex.qtum.dataprovider.listeners.TransactionListener;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.dataprovider.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.tokenBalance.TokenBalance;
import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.utils.DateCalculator;
import com.pixelplex.qtum.utils.QtumIntent;
import com.pixelplex.qtum.datastorage.TinyDB;

import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UpdateService extends Service {

    private final int DEFAULT_NOTIFICATION_ID = 101;
    private NotificationManager notificationManager;
    private TransactionListener mTransactionListener = null;
    private BalanceChangeListener mBalanceChangeListener;
    private HashMap<String,TokenBalanceChangeListener> mStringTokenBalanceChangeListenerHashMap = new HashMap<>();
    private TokenListener mTokenListener;
    private boolean monitoringFlag = false;
    private Notification notification;
    private Socket socket;
    private int totalTransaction = 0;
    private JSONArray mAddresses;

    private String mFirebasePrevToken;
    private String mFirebaseCurrentToken;

    private String mBalance = null;
    private String mUnconfirmedBalance = null;

    private UpdateBinder mUpdateBinder = new UpdateBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            socket = IO.socket("http://163.172.68.103:5931/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String[] firebaseTokens = QtumSharedPreference.getInstance().getFirebaseTokens(getApplicationContext());
        mFirebasePrevToken = firebaseTokens[0];
        mFirebaseCurrentToken = firebaseTokens[1];

        QtumSharedPreference.getInstance().addFirebaseTokenRefreshListener(new FireBaseTokenRefreshListener() {
            @Override
            public void onRefresh(String prevToken, String currentToken) {
                mFirebasePrevToken = prevToken;
                mFirebaseCurrentToken = currentToken;
                subscribeSocket();
            }
        });

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
                    BigDecimal unconfirmedBalance;
                    BigDecimal balance;
                    try {
                        unconfirmedBalance = (new BigDecimal(data.getString("unconfirmedBalance"))).divide(new BigDecimal("100000000"));
                        balance = (new BigDecimal(data.getString("balance"))).divide(new BigDecimal("100000000"));
                        mBalance = balance.toString();
                        mUnconfirmedBalance = unconfirmedBalance.toString();
                        HistoryList.getInstance().setBalance(balance.toString());
                        HistoryList.getInstance().setUnconfirmedBalance(unconfirmedBalance.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mBalanceChangeListener != null) {
                        mBalanceChangeListener.onChangeBalance();
                    }
                } catch (ClassCastException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).on("new_transaction", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new Gson();
                JSONObject data = (JSONObject) args[0];
                History history = gson.fromJson(data.toString(), History.class);
                if(((QtumApplication)getApplication()).isContractAwait()){
                    TinyDB tinyDB = new TinyDB(getApplicationContext());

                    boolean done = false;

                    List<Contract> contractListWithoutToken = tinyDB.getContractListWithoutToken();
                    for(Contract contract : contractListWithoutToken){
                        if(contract.getContractAddress()==null){
                            contract.setContractAddress(generateContractAddress(history.getTxHash()));
                            done = true;
                            break;
                        }
                    }
                    tinyDB.putContractListWithoutToken(contractListWithoutToken);

                    if(!done) {
                        List<Token> tokenList = tinyDB.getTokenList();
                        for (Token token : tokenList) {
                            if (token.getContractAddress() == null) {
                                token.setContractAddress(generateContractAddress(history.getTxHash()));
                                break;
                            }
                        }
                        tinyDB.putTokenList(tokenList);
                    }

                    ((QtumApplication)getApplication()).setContractAwait(false);
                }
                if(history.getContractHasBeenCreated()!=null && history.getContractHasBeenCreated() && history.getBlockTime() != null){

                    String txHash = history.getTxHash();
                    String contractAddress = generateContractAddress(txHash);

                    TinyDB tinyDB = new TinyDB(getApplicationContext());

                    boolean done = false;

                    List<Contract> contractList = tinyDB.getContractListWithoutToken();
                    for(Contract contract : contractList){
                        if(contract.getContractAddress()!=null && contract.getContractAddress().equals(contractAddress)){
                            contract.setHasBeenCreated(true);
                            contract.setDate(DateCalculator.getDateInFormat(history.getBlockTime()*1000L));
                            done = true;
                            break;
                        }
                    }
                    tinyDB.putContractListWithoutToken(contractList);

                    if(!done){
                        List<Token> tokenList = tinyDB.getTokenList();
                        for(Token token : tokenList){
                            if(token.getContractAddress()!=null && token.getContractAddress().equals(contractAddress)){
                                token.setHasBeenCreated(true);
                                token.setDate(DateCalculator.getDateInFormat(history.getBlockTime()*1000L));
                                break;
                            }
                        }
                        tinyDB.putTokenList(tokenList);
                    }

                    subscribeTokenBalanceChange(contractAddress, mFirebasePrevToken, mFirebaseCurrentToken);
                }
                if (mTransactionListener != null) {
                    mTransactionListener.onNewHistory(history);
                    if (!mTransactionListener.getVisibility()) {
                        if (history.getBlockTime() != null) {
                            totalTransaction++;
                            sendNotification("New confirmed transaction", totalTransaction + " new confirmed transaction",
                                    "Touch to open transaction history", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        }
                    }
                } else {
                    if (history.getBlockTime() != null) {
                        totalTransaction++;
                        sendNotification("New confirmed transaction", totalTransaction + " new confirmed transaction",
                                "Touch to open transaction history", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    }
                }

            }
        }).on("token_balance_change", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new Gson();
                JSONObject data = (JSONObject) args[0];
                TokenBalance tokenBalance = gson.fromJson(data.toString(), TokenBalance.class);

                TokenBalanceChangeListener tokenBalanceChangeListener = mStringTokenBalanceChangeListenerHashMap.get(tokenBalance.getContractAddress());
                if(tokenBalanceChangeListener!=null){
                    tokenBalanceChangeListener.onBalanceChange(tokenBalance);
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("socket","disconnect");
            }
        });

        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMonitoring();
        stopSelf();
    }

    private void subscribeSocket(){
        subscribeBalanceChange(mFirebasePrevToken,mFirebaseCurrentToken);
        for(Contract contract : (new TinyDB(getApplicationContext())).getContractList()){
            subscribeTokenBalanceChange(contract.getContractAddress(),mFirebasePrevToken,mFirebaseCurrentToken);
        }
    }

    private void subscribeTokenBalanceChange(String tokenAddress, String prevToken, String currentToken){
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectToken = new JSONObject();
        try {
            jsonObject.put("contract_address",tokenAddress);
            jsonObject.put("addresses",mAddresses);

            jsonObjectToken.put("notificationToken",currentToken);
            jsonObjectToken.put("prevToken",prevToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("subscribe","token_balance_change",jsonObject, jsonObjectToken);
    }

    private void subscribeBalanceChange(String prevToken, String currentToken){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("notificationToken",currentToken);
            jsonObject.put("prevToken",prevToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("subscribe", "balance_subscribe", mAddresses, jsonObject);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(QtumSharedPreference.getInstance().getKeyGeneratedInstance(getBaseContext())){
            startMonitoring();
        }
        return START_REDELIVER_INTENT;
    }

    private String generateContractAddress(String txHash){
        char[] ca = txHash.toCharArray();
        StringBuilder sb = new StringBuilder(txHash.length());
        for (int i = 0; i < txHash.length(); i += 2) {
            sb.insert(0, ca, i, 2);
        }

        String reverse_tx_hash = sb.toString();
        reverse_tx_hash = reverse_tx_hash.concat("00000000");


        byte[] test5 = Hex.decode(reverse_tx_hash);

        SHA256Digest sha256Digest = new SHA256Digest();
        sha256Digest.update(test5, 0, test5.length);
        byte[] out = new byte[sha256Digest.getDigestSize()];
        sha256Digest.doFinal(out, 0);

        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(out, 0, out.length);
        byte[] out2 = new byte[ripemd160Digest.getDigestSize()];
        ripemd160Digest.doFinal(out2, 0);

        return Hex.toHexString(out2);
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

    public void startMonitoring() {
        if (!monitoringFlag) {
            if (mAddresses != null) {
                socket.connect();
                monitoringFlag = true;
            } else {
                loadWalletFromFile(new LoadWalletFromFileCallBack() {
                    @Override
                    public void onSuccess() {
                        mAddresses = new JSONArray();
                        for (String address : KeyStorage.getInstance().getAddresses()) {
                            mAddresses.put(address);
                        }
                        startMonitoring();
                    }
                });
            }
        }
    }

    public void stopMonitoring() {
        socket.emit("unsubscribe", "quantumd/addressbalance");
        socket.disconnect();
        monitoringFlag = false;
        mAddresses = null;
        stopForeground(false);
        notificationManager.cancel(DEFAULT_NOTIFICATION_ID);
    }

    private void sendNotification(String Ticker, String Title, String Text, Uri sound) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(QtumIntent.OPEN_FROM_NOTIFICATION);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(contentIntent)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setAutoCancel(true)
                .setTicker(Ticker)
                .setContentTitle(Title)
                .setContentText(Text)
                .setWhen(System.currentTimeMillis())
                .setSound(sound);

        if (android.os.Build.VERSION.SDK_INT <= 21) {
            builder.setSmallIcon(R.drawable.ic_launcher);
        } else {
            builder.setSmallIcon(R.drawable.logo);
        }
        notification = builder.build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mUpdateBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        if(mBalance!=null){
            HistoryList.getInstance().setUnconfirmedBalance(mUnconfirmedBalance);
            HistoryList.getInstance().setBalance(mBalance);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void addTransactionListener(TransactionListener updateServiceListener) {
        mTransactionListener = updateServiceListener;
    }

    public void removeTransactionListener() {
        mTransactionListener = null;
    }

    public void addBalanceChangeListener(BalanceChangeListener balanceChangeListener) {
        mBalanceChangeListener = balanceChangeListener;
    }

    public void addTokenListener(TokenListener tokenListener){
        mTokenListener = tokenListener;
    }

    public void removeTokenListener(){
        mTokenListener = null;
    }

    public void addTokenBalanceChangeListener(String address, TokenBalanceChangeListener tokenBalanceChangeListener){
        mStringTokenBalanceChangeListenerHashMap.put(address,tokenBalanceChangeListener);
    }

    public void removeTokenBalanceChangeListener(String address){
        mStringTokenBalanceChangeListenerHashMap.remove(address);
    }

    public void removeBalanceChangeListener() {
        mBalanceChangeListener = null;
    }

    public void clearNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        totalTransaction = 0;
    }


    public class UpdateBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}