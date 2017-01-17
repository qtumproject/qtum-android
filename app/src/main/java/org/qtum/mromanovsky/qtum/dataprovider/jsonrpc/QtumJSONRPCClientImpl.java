package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;
import org.json.JSONArray;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class QtumJSONRPCClientImpl extends JSONRPCHttpClient implements  QtumJSONRPCClient{

    QtumJSONRPCClientImpl mQtumJSONRPCClient = this;
    @Override
    public Observable<List<TransactionQTUM>> getHistory(final String identifier) {
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
    public Observable<String[]> registerKey(final String key, final String identifier) {
        return  Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                ECKey ecKey = new ECKey();
                Address address = ecKey.toAddress(MainNetParams.get());
                final Object[] params = new Object[4];
                params[0] = address.toString();
                params[1] = identifier;
                params[2] = false;
                params[3] = false;
                final String method = "importaddress";
                try {
                    mQtumJSONRPCClient.call(method,params);
                    String[] array = new String[2];
                    array[0]=address.toString();
                    array[1]=identifier;
                    subscriber.onNext(array);
                } catch (JSONRPCException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
