package org.qtum.mromanovsky.qtum.dataprovider.jsonrpc;


import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.List;

import rx.Observable;

public interface QtumJSONRPCClient {
    Observable<List<TransactionQTUM>> getTransactions(String identifier);
    Observable<String[]> generateRegisterKeyAndIdentifier();
}
