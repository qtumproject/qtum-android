package org.qtum.mromanovsky.qtum.datastorage;


import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.ArrayList;
import java.util.List;

public class TransactionQTUMList {
    private static TransactionQTUMList sTransactionQTUMList;

    private List<TransactionQTUM> mTransactionQTUMList;

    private TransactionQTUMList() {
        mTransactionQTUMList = new ArrayList<>();
    }

    public static TransactionQTUMList getInstance() {
        if (sTransactionQTUMList == null) {
            sTransactionQTUMList = new TransactionQTUMList();
        }
        return sTransactionQTUMList;
    }

    public List<TransactionQTUM> getTransactionQTUMList() {
        return mTransactionQTUMList;
    }

    public void setTransactionQTUMList(List<TransactionQTUM> transactionQTUMList) {
        mTransactionQTUMList = transactionQTUMList;
    }
}
