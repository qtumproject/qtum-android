package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.qtum.mromanovsky.qtum.btc.BTCUtils;
import org.qtum.mromanovsky.qtum.btc.KeyPair;
import org.qtum.mromanovsky.qtum.btc.Transaction;
import org.qtum.mromanovsky.qtum.btc.UnspentOutputInfo;
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
                //try {
                    //mQtumJSONRPCClient.call(method,params);
                    String[] array = new String[2];
                    array[0] = String.valueOf(params[0]);
                    array[1] = String.valueOf(params[1]);
                    subscriber.onNext(array);
//                } catch (JSONRPCException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    public Observable<ArrayList<UnspentOutputInfo>> getUnspentOutputInfo(final String address) {
        return Observable.create(new Observable.OnSubscribe<ArrayList<UnspentOutputInfo>>() {
            @Override
            public void call(Subscriber<? super ArrayList<UnspentOutputInfo>> subscriber) {
                Object[] params = new Object[3];
                params[0] = 0;
                params[1] = 20000;
                params[2] = address;
                String method = "listunspent";

                Gson gson = new Gson();
                JSONArray jsonArray = null;
                try {
                    jsonArray = callJSONArray(method,params);
                    List<UnspentOutputResponse> unspentOutputResponseList = gson.fromJson(jsonArray.toString(),new TypeToken<List<UnspentOutputResponse>>(){}.getType());
                    ArrayList<UnspentOutputInfo> unspentOutputs = new ArrayList<>();
                    for(UnspentOutputResponse unspentOutputResponse : unspentOutputResponseList) {
                        byte[] txHash = BTCUtils.reverse(BTCUtils.fromHex(unspentOutputResponse.getTxid()));
                        Transaction.Script script = new Transaction.Script(BTCUtils.fromHex(unspentOutputResponse.getScriptPubKey()));
                        double value = unspentOutputResponse.getAmount();
                        long confirmations = unspentOutputResponse.getConfirmations();
                        int outputIndex = unspentOutputResponse.getVout();
                        unspentOutputs.add(new UnspentOutputInfo(txHash, script, value, outputIndex, confirmations));
                    }
                    subscriber.onNext(unspentOutputs);
                } catch (JSONRPCException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public Observable<Boolean> sendTx(final String txHash) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Object[] params = new Object[2];
                params[0] = txHash;
                params[1] = true;
                String method = "sendrawtransaction";
                try {
                    mQtumJSONRPCClient.call(method,params);
                    subscriber.onNext(true);
                } catch (JSONRPCException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}