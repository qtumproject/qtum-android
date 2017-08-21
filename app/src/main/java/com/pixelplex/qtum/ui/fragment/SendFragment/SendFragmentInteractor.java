package com.pixelplex.qtum.ui.fragment.SendFragment;


import com.pixelplex.qtum.model.contract.Token;
import java.util.List;

interface SendFragmentInteractor {
    void getUnspentOutputs(SendFragmentInteractorImpl.GetUnspentListCallBack callBack);
    void sendTx(String address, String amount, SendFragmentInteractorImpl.SendTxCallBack callBack);
    void sendTx(String txHex, SendFragmentInteractorImpl.SendTxCallBack callBack);
    void createTx(String address, String amount, SendFragmentInteractorImpl.CreateTxCallBack callBack);
    String getPassword();
    List<String> getAddresses();
    List<Token> getTokenList();
}
