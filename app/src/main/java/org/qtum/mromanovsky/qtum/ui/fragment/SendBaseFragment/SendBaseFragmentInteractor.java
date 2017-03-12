package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;


public interface SendBaseFragmentInteractor {
    void getUnspentOutputs(SendBaseFragmentInteractorImpl.GetUnspentListCallBack callBack);
    void sendTx(String address, String amount, SendBaseFragmentInteractorImpl.SendTxCallBack callBack);
    void createTx(String address, String amount, SendBaseFragmentInteractorImpl.CreateTxCallBack callBack);
    int getPassword();
    double getBalance();
}
