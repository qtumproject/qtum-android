package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.qtum.mromanovsky.qtum.btc.BTCUtils;
import org.qtum.mromanovsky.qtum.btc.KeyPair;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

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
    public Observable<String[]> generateRegisterKeyAndIdentifier() {
        return  Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                KeyPair keyPair = BTCUtils.generateWifKey(true);

                final Object[] params = new Object[4];
                params[0] = keyPair.address;
                params[1] = "random001";
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
}
