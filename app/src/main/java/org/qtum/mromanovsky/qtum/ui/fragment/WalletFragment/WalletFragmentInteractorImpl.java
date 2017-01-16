package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.JSONRPCThreadedClient;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.JSONRPCThreadedHttpClient;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.datastorage.Transactions;
import org.qtum.mromanovsky.qtum.model.History;
import org.qtum.mromanovsky.qtum.model.Transaction;

import java.util.List;

public class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    double mBalance = 0;
    Context mContext;

    public WalletFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public List<Transaction> getTransactionList() {
        List<Transaction> transactionList = Transactions.getInstance().getTransactionList();
        transactionList.add(new Transaction("5odqs25oxasmewqo4129d", "Sep 23", 7.450, "231dcasdawe123123lcasd", "231dcasdawe123123lcasd"));
        transactionList.add(new Transaction("4129d5odqs25oxasmewqo", "Nov 03", -7.450, "231dcasdawe123123lcasd", "231dcasdawe123123lcasd"));
        transactionList.add(new Transaction("oxasmew5odqs25qo4129d", "Dec 16", 0.930, "231dcasdawe123123lcasd", "231dcasdawe123123lcasd"));
        return transactionList;
    }

    @Override
    public void getBalance(JSONRPCThreadedClient.OnJSONArrayResultListener jsonArrayResultListener) {
        JSONRPCThreadedHttpClient jsonrpcThreadedHttpClient = new JSONRPCThreadedHttpClient();
        jsonrpcThreadedHttpClient.getHistory("testIdentifier1000", jsonArrayResultListener);
    }

    @Override
    public String getPubKey() {
        return QtumSharedPreference.getInstance().getPubKey(mContext);
    }
}
