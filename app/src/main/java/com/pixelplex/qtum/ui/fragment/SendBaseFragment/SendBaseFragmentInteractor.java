package com.pixelplex.qtum.ui.fragment.SendBaseFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Token;

import java.util.List;

interface SendBaseFragmentInteractor {
    void getUnspentOutputs(SendBaseFragmentInteractorImpl.GetUnspentListCallBack callBack);
    void sendTx(String address, String amount, SendBaseFragmentInteractorImpl.SendTxCallBack callBack);
    void sendTx(String txHex, SendBaseFragmentInteractorImpl.SendTxCallBack callBack);
    void createTx(String address, String amount, SendBaseFragmentInteractorImpl.CreateTxCallBack callBack);
    int getPassword();
    String getBalance();
    List<String> getAddresses();
    List<Token> getTokenList();
}
