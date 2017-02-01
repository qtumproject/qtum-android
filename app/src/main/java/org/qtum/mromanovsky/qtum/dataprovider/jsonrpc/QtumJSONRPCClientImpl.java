package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.bitcoinj.core.ECKey;
import org.json.JSONArray;
import org.qtum.mromanovsky.qtum.btc.BTCUtils;
import org.qtum.mromanovsky.qtum.btc.KeyPair;
import org.qtum.mromanovsky.qtum.btc.Transaction;
import org.qtum.mromanovsky.qtum.btc.UnspentOutputInfo;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;
import org.qtum.mromanovsky.qtum.model.UnspentOutputResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class QtumJSONRPCClientImpl extends JSONRPCHttpClient implements  QtumJSONRPCClient{

    private QtumJSONRPCClientImpl mQtumJSONRPCClient = this;

    @Override
    public Observable<List<TransactionQTUM>> getTransactions(final String identifier) {
        return Observable.create(new Observable.OnSubscribe<List<TransactionQTUM>>() {
            @Override
            public void call(Subscriber<? super List<TransactionQTUM>> subscriber) {
                try {
                    final Object[] params = new Object[4];
                    params[0] = identifier;
                    params[1] = 10000000;
                    params[2] = 0;
                    params[3] = true;
                    final String method = "listtransactions";
                    Gson gson = new Gson();
                    JSONArray jsonArray = callJSONArray(method,params);
                    List<TransactionQTUM> transactionList = gson.fromJson(jsonArray.toString(),new TypeToken<List<TransactionQTUM>>(){}.getType());
                    subscriber.onNext(transactionList);
                } catch (JSONRPCException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Observable<String[]> generateRegisterKeyAndIdentifier(final Context context) {
        return  Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                KeyStorage.getInstance(context).getWallet();
                final Object[] params = new Object[4];
                params[0] = KeyStorage.getInstance(context).getWallet().currentReceiveAddress().toString();
                params[1] = params[0];
                params[2] = false;
                params[3] = false;
                final String method = "importaddress";
                try {
                    mQtumJSONRPCClient.call(method,params);
                    String[] array = new String[2];
                    array[0] = String.valueOf(params[0]);
                    array[1] = String.valueOf(params[1]);
                    subscriber.onNext(array);
                } catch (JSONRPCException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Observable<List<UnspentOutputResponse>> getUnspentOutputInfo(final String address) {
        return Observable.create(new Observable.OnSubscribe<List<UnspentOutputResponse>>() {
            @Override
            public void call(Subscriber<? super List<UnspentOutputResponse>> subscriber) {
                Object[] params = new Object[3];
                params[0] = 0;
                params[1] = 20000;

                String[] strings = new String[1];
                strings[0] = address;

                params[2] = strings;
                String method = "listunspent";

                Gson gson = new Gson();
                JSONArray jsonArray = null;
                try {
                    jsonArray = callJSONArray(method,params);
                    List<UnspentOutputResponse> unspentOutputResponseList = gson.fromJson(jsonArray.toString(),new TypeToken<List<UnspentOutputResponse>>(){}.getType());
                    subscriber.onNext(unspentOutputResponseList);
                } catch (JSONRPCException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public Observable<Boolean> sendTx(final String txHex) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Object[] params = new Object[2];
                params[0] = txHex;
                params[1] = true;
                String method = "sendrawtransaction";
                try {
                    mQtumJSONRPCClient.call(method,params);
                    subscriber.onNext(true);
                } catch (JSONRPCException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}