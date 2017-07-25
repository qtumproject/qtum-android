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

import com.google.gson.JsonObject;
import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.firebase.FirebaseSharedPreferences;
import com.pixelplex.qtum.dataprovider.listeners.BalanceChangeListener;
import com.pixelplex.qtum.dataprovider.listeners.FireBaseTokenRefreshListener;
import com.pixelplex.qtum.dataprovider.listeners.TokenListener;
import com.pixelplex.qtum.dataprovider.listeners.TransactionListener;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.dataprovider.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.model.gson.CallSmartContractRequest;
import com.pixelplex.qtum.model.gson.callSmartContractResponse.CallSmartContractResponse;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.tokenBalance.TokenBalance;
import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.ContractManagementFragment.ContractManagementFragmentPresenter;
import com.pixelplex.qtum.utils.ContractBuilder;
import com.pixelplex.qtum.utils.DateCalculator;
import com.pixelplex.qtum.utils.QtumIntent;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.utils.sha3.sha.Keccak;
import com.pixelplex.qtum.utils.sha3.sha.Parameters;

import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UpdateService extends Service {

    private final int DEFAULT_NOTIFICATION_ID = 101;
    private NotificationManager notificationManager;
    private TransactionListener mTransactionListener = null;
    private BalanceChangeListener mBalanceChangeListener;
    private HashMap<String,TokenBalanceChangeListener> mStringTokenBalanceChangeListenerHashMap = new HashMap<>();
    private HashMap<String, TokenBalance> mAllTokenBalanceList = new HashMap<>();
    private TokenListener mTokenListener;
    private boolean monitoringFlag = false;
    private Notification notification;
    private Socket socket;
    private int totalTransaction = 0;
    private JSONArray mAddresses;

    private String mFirebasePrevToken;
    private String mFirebaseCurrentToken;

    private UpdateBinder mUpdateBinder = new UpdateBinder();
    String[] firebaseTokens;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            socket = IO.socket("http://163.172.68.103:5931/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        checkConfirmContract();

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

                if(history.getContractHasBeenCreated()!=null && history.getContractHasBeenCreated() && history.getBlockTime() != null){

                    String txHash = history.getTxHash();
                    String contractAddress = ContractBuilder.generateContractAddress(txHash);

                    TinyDB tinyDB = new TinyDB(getApplicationContext());

                    boolean done = false;

                    List<Contract> contractList = tinyDB.getContractListWithoutToken();
                    for(Contract contract : contractList){
                        if(contract.getContractAddress()!=null && contract.getContractAddress().equals(contractAddress)){
                            contract.setHasBeenCreated(true);
                            contract.setDate(DateCalculator.getDateInFormat(history.getBlockTime()*1000L));
                            done = true;
                            ArrayList<String> unconfirmedContractTxHashList = tinyDB.getUnconfirmedContractTxHasList();
                            unconfirmedContractTxHashList.remove(history.getTxHash());
                            tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
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
                                ArrayList<String> unconfirmedContractTxHashList = tinyDB.getUnconfirmedContractTxHasList();
                                unconfirmedContractTxHashList.remove(history.getTxHash());
                                tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
                                break;
                            }
                        }
                        tinyDB.putTokenList(tokenList);
                        if(mTokenListener!=null) {
                            mTokenListener.newToken();
                        }
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
                mAllTokenBalanceList.put(tokenBalance.getContractAddress(),tokenBalance);
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

    private void checkConfirmContract() {
        final TinyDB tinyDB = new TinyDB(getApplicationContext());
        final ArrayList<String> unconfirmedContractTxHashList = tinyDB.getUnconfirmedContractTxHasList();
        for(final String unconfirmedContractTxHash : unconfirmedContractTxHashList){
            QtumService.newInstance()
                    .getTransaction(unconfirmedContractTxHash)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<History>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(History history) {
                            if(history.getContractHasBeenCreated()!=null && history.getContractHasBeenCreated() && history.getBlockTime() != null){

                                String contractAddress = ContractBuilder.generateContractAddress(unconfirmedContractTxHash);

                                boolean done = false;

                                List<Contract> contractList = tinyDB.getContractListWithoutToken();
                                for(Contract contract : contractList){
                                    if(contract.getContractAddress()!=null && contract.getContractAddress().equals(contractAddress)){
                                        contract.setHasBeenCreated(true);
                                        contract.setDate(DateCalculator.getDateInFormat(history.getBlockTime()*1000L));
                                        done = true;
                                        unconfirmedContractTxHashList.remove(history.getTxHash());
                                        tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
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
                                            unconfirmedContractTxHashList.remove(history.getTxHash());
                                            tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
                                            break;
                                        }
                                    }
                                    tinyDB.putTokenList(tokenList);
                                    if(mTokenListener!=null) {
                                        mTokenListener.newToken();
                                    }
                                }

                                subscribeTokenBalanceChange(contractAddress, mFirebasePrevToken, mFirebaseCurrentToken);
                            }
                        }
                    });
        }
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
        return START_NOT_STICKY;
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
        JSONObject obj = new JSONObject();
        try {
            obj.put("notificationToken", mFirebaseCurrentToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("unsubscribe", "token_balance_change", null, obj);
        socket.emit("unsubscribe", "balance_subscribe", null, obj);
        socket.disconnect();
        monitoringFlag = false;
        mAddresses = null;
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
        balanceChangeListener.onChangeBalance();
    }

    public void addTokenListener(TokenListener tokenListener){
        mTokenListener = tokenListener;
    }

    public void removeTokenListener(){
        mTokenListener = null;
    }

    public void addTokenBalanceChangeListener(String address, TokenBalanceChangeListener tokenBalanceChangeListener){
        mStringTokenBalanceChangeListenerHashMap.put(address,tokenBalanceChangeListener);
        TokenBalance tokenBalance = mAllTokenBalanceList.get(address);
        if(tokenBalance!=null){
            tokenBalanceChangeListener.onBalanceChange(tokenBalance);
        }
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