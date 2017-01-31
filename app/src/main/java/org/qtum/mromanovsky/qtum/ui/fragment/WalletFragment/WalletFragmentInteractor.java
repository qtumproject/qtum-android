package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.List;

public interface WalletFragmentInteractor {
    List<TransactionQTUM> getTransactionList();
    void getTransaction(WalletFragmentInteractorImpl.GetDataCallBack callBack);
    String getAddress();
}