package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendFragment;


import org.qtum.mromanovsky.qtum.btc.Transaction;


public interface SendFragmentInteractor {
    void createTx(String address, String amount, SendFragmentInteractorImpl.CreateTxCallBack callBack);
    void sendTx(Transaction transaction, SendFragmentInteractorImpl.SendTxCallBack callBack);
}
