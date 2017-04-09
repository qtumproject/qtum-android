package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;


import java.util.List;

interface SendBaseFragmentInteractor {
    void getUnspentOutputs(SendBaseFragmentInteractorImpl.GetUnspentListCallBack callBack);
    void sendTx(String address, String amount, SendBaseFragmentInteractorImpl.SendTxCallBack callBack);
    void createTx(String address, String amount, SendBaseFragmentInteractorImpl.CreateTxCallBack callBack);
    int getPassword();
    String getBalance();
    List<String> getAddresses();
}
