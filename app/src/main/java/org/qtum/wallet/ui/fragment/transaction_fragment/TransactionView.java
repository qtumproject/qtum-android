package org.qtum.wallet.ui.fragment.transaction_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

import io.realm.Realm;

public interface TransactionView extends BaseFragmentView {
    void setUpTransactionData(String value, String symbol,String fee,String receivedTime, boolean confirmed, boolean isContractCall);
    Realm getRealm();
    int getDecimalUnits();
    String getSymbol();
}
