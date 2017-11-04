package org.qtum.wallet.ui.fragment.send_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.FeePerKb;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

public interface SendInteractor {
    void getUnspentOutputs(SendInteractorImpl.GetUnspentListCallBack callBack);

    void getUnspentOutputs(String address, final SendInteractorImpl.GetUnspentListCallBack callBack);

    void sendTx(String from, String address, String amount, String fee, SendInteractorImpl.SendTxCallBack callBack);

    void sendTx(String txHex, SendInteractorImpl.SendTxCallBack callBack);

    void createTx(String from, String address, String amount, String fee, BigDecimal estimateFeePerKb, SendInteractorImpl.CreateTxCallBack callBack);

    List<String> getAddresses();

    List<Token> getTokenList();

    String validateTokenExistance(String tokenAddress);

    String getValidatedFee(Double fee);

    String createTransactionHash(String abiParams, String contractAddress, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee);

    Observable<String> createAbiMethodParamsObservable(String address, String resultAmount, String transfer);

    Observable<CallSmartContractResponse> callSmartContractObservable(Token token, String hash, String fromAddress);

    BigDecimal getFeePerKb();

    int getMinGasPrice();

}