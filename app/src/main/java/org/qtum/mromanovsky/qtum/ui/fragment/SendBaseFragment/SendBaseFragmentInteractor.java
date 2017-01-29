package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;


import org.qtum.mromanovsky.qtum.btc.Transaction;


public interface SendBaseFragmentInteractor {
    void createTx(String address, String amount, SendBaseFragmentInteractorImpl.CreateTxCallBack callBack);
    void sendTx(Transaction transaction, SendBaseFragmentInteractorImpl.SendTxCallBack callBack);
    int getPassword();
}
