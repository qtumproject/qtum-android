package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;


import org.qtum.mromanovsky.qtum.btc.Transaction;
import org.qtum.mromanovsky.qtum.model.UnspentOutputResponse;

import java.util.List;


public interface SendBaseFragmentInteractor {
    void getUnspentOutputList(SendBaseFragmentInteractorImpl.GetUnspentListCallBack callBack);
    void sendTx(String address, String amount, SendBaseFragmentInteractorImpl.SendTxCallBack callBack);
    void createTx(String address, String amount, SendBaseFragmentInteractorImpl.CreateTxCallBack callBack);
    int getPassword();
}
