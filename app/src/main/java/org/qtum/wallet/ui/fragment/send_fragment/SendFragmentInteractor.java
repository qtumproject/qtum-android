package org.qtum.wallet.ui.fragment.send_fragment;


import org.qtum.wallet.model.contract.Token;

import java.math.BigDecimal;
import java.util.List;

interface SendFragmentInteractor {
    void getUnspentOutputs(SendFragmentInteractorImpl.GetUnspentListCallBack callBack);
    void sendTx(String address, String amount, String fee, SendFragmentInteractorImpl.SendTxCallBack callBack);
    void sendTx(String txHex, SendFragmentInteractorImpl.SendTxCallBack callBack);
    void createTx(String address, String amount, String fee, BigDecimal estimateFeePerKb, SendFragmentInteractorImpl.CreateTxCallBack callBack);
    String getPassword();
    List<String> getAddresses();
    List<Token> getTokenList();
}
