package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;


import android.content.Context;

import org.qtum.mromanovsky.qtum.btc.UnspentOutputInfo;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;
import org.qtum.mromanovsky.qtum.model.UnspentOutputResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public interface QtumJSONRPCClient {
    Observable<List<TransactionQTUM>> getTransactions(String identifier);
    Observable<String[]> generateRegisterKeyAndIdentifier(Context context);
    Observable<List<UnspentOutputResponse>> getUnspentOutputInfo(String address);
    Observable<Boolean> sendTx(String txHash);
}
