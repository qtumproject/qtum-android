package org.qtum.wallet.ui.fragment.contract_confirm_fragment;


import android.content.Context;

import org.bitcoinj.script.Script;
import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.QtumNetworkState;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.utils.ContractBuilder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

class ContractConfirmInteractorImpl implements ContractConfirmInteractor{

    Context mContext;

    ContractConfirmInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public Observable<String> createAbiConstructParams(List<ContractMethodParameter> contractMethodParameterList, String uiid) {
        ContractBuilder contractBuilder = new ContractBuilder();
        return contractBuilder.createAbiConstructParams(contractMethodParameterList, uiid ,mContext);
    }

    @Override
    public Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses(List<String> addresses) {
        return QtumService.newInstance().getUnspentOutputsForSeveralAddresses(addresses);
    }

    @Override
    public Observable<SendRawTransactionResponse> sendRawTransaction(SendRawTransactionRequest sendRawTransactionRequest) {
        return QtumService.newInstance().sendRawTransaction(sendRawTransactionRequest);
    }

    @Override
    public void saveContract(String txid, String contractTemplateUiid, String contractName, String senderAddress) {
        TinyDB tinyDB = new TinyDB(mContext);
        ArrayList<String> unconfirmedTokenTxHashList = tinyDB.getUnconfirmedContractTxHasList();
        unconfirmedTokenTxHashList.add(txid);
        tinyDB.putUnconfirmedContractTxHashList(unconfirmedTokenTxHashList);
        for (ContractTemplate contractTemplate : tinyDB.getContractTemplateList()) {
            if (contractTemplate.getUuid().equals(contractTemplateUiid)) {
                if (contractTemplate.getContractType().equals("token")) {
                    Token token = new Token(ContractBuilder.generateContractAddress(txid), contractTemplateUiid, false, null, senderAddress, contractName);
                    List<Token> tokenList = tinyDB.getTokenList();
                    tokenList.add(token);
                    tinyDB.putTokenList(tokenList);
                } else {
                    Contract contract = new Contract(ContractBuilder.generateContractAddress(txid), contractTemplateUiid, false, null, senderAddress, contractName);
                    List<Contract> contractList = tinyDB.getContractListWithoutToken();
                    contractList.add(contract);
                    tinyDB.putContractListWithoutToken(contractList);
                }
            }
        }
    }

    @Override
    public String createTransactionHash(Script script, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee) {
        ContractBuilder contractBuilder = new ContractBuilder();
        return contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice, QtumNetworkState.newInstance().getFeePerKb().getFeePerKb(), fee, mContext);
    }

    @Override
    public double getMinFee() {
        return QtumNetworkState.newInstance().getFeePerKb().getFeePerKb().doubleValue();
    }

    @Override
    public int getMinGasPrice() {
        return QtumNetworkState.newInstance().getDGPInfo().getMingasprice();
    }
}
