package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.List;

public interface WalletFragmentInteractor {
    List<TransactionQTUM> getTransactionList();
    void getData(String identifier, WalletFragmentInteractorImpl.GetDataCallBack callBack);
    String getPubKey();
}