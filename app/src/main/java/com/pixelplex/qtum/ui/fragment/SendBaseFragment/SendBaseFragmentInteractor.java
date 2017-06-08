package com.pixelplex.qtum.ui.fragment.SendBaseFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;

import java.util.List;

interface SendBaseFragmentInteractor {
    void getUnspentOutputs(SendBaseFragmentInteractorImpl.GetUnspentListCallBack callBack);
    void sendTx(String address, String amount, SendBaseFragmentInteractorImpl.SendTxCallBack callBack);
    void createTx(String address, String amount, SendBaseFragmentInteractorImpl.CreateTxCallBack callBack);
    int getPassword();
    String getBalance();
    List<String> getAddresses();
    List<ContractInfo> getContractList();
}
