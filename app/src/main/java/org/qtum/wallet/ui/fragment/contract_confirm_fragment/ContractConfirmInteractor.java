package org.qtum.wallet.ui.fragment.contract_confirm_fragment;


import org.bitcoinj.script.Script;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;

import java.util.List;

import rx.Observable;

public interface ContractConfirmInteractor {
    Observable<String> createAbiConstructParams(List<ContractMethodParameter> contractMethodParameterList,String uiid);
    Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses(List<String> addresses);
    Observable<SendRawTransactionResponse> sendRawTransaction(SendRawTransactionRequest sendRawTransactionRequest);
    void saveContract(String txid, String contractTemplateUiid, String contractName, String senderAddress);
    String createTransactionHash(Script script, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee);
    double getMinFee();
    int getMinGasPrice();
}
