package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class QtumJSONRPCClientImpl extends JSONRPCHttpClient implements  QtumJSONRPCClient{

    @Override
    public Observable<List<TransactionQTUM>> getHistory(String identifier) {

        final Object[] params = new Object[4];
        params[0] = identifier;
        params[1] = 10000000;
        params[2] = 0;
        params[3] = true;
        final String method = "listtransactions";

        return Observable.create(new Observable.OnSubscribe<List<TransactionQTUM>>() {
            @Override
            public void call(Subscriber<? super List<TransactionQTUM>> subscriber) {
                try {
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
}
