package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;


import org.qtum.mromanovsky.qtum.btc.UnspentOutputInfo;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public interface QtumJSONRPCClient {
    Observable<List<TransactionQTUM>> getTransactions(String identifier);
    Observable<String[]> generateRegisterKeyAndIdentifier();
    Observable<ArrayList<UnspentOutputInfo>> getUnspentOutputInfo(String address);
    Observable<Boolean> sendTx(String txHash);
}
