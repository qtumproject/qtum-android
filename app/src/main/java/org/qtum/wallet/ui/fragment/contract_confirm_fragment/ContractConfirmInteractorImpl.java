package org.qtum.wallet.ui.fragment.contract_confirm_fragment;


import android.content.Context;

import org.bitcoinj.script.Script;
import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.utils.ContractBuilder;

import java.math.BigDecimal;
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
    public Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses() {
        return QtumService.newInstance().getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses());
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
                    saveToken(tinyDB, txid, contractTemplateUiid, contractName, senderAddress);
                } else {
                    saveContract(tinyDB, txid, contractTemplateUiid, contractName, senderAddress);
                    if(contractTemplate.getContractType().equals("crowdsale")){
                        saveToken(tinyDB, txid, contractTemplateUiid, contractName, senderAddress);
                    }
                }
            }
        }
    }

    private void saveToken(TinyDB tinyDB, String txid,String contractTemplateUiid, String contractName, String senderAddress) {
        Token token = new Token(ContractBuilder.generateContractAddress(txid), contractTemplateUiid, false, null, senderAddress, contractName);
        List<Token> tokenList = tinyDB.getTokenList();
        tokenList.add(token);
        tinyDB.putTokenList(tokenList);
    }

    private void saveContract(TinyDB tinyDB, String txid,String contractTemplateUiid, String contractName, String senderAddress) {
        Contract contract = new Contract(ContractBuilder.generateContractAddress(txid), contractTemplateUiid, false, null, senderAddress, contractName);
        List<Contract> contractList = tinyDB.getContractListWithoutToken();
        contractList.add(contract);
        tinyDB.putContractListWithoutToken(contractList);
    }

    @Override
    public String createTransactionHash(String abiParams, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee) {
        ContractBuilder contractBuilder = new ContractBuilder();
        Script script = contractBuilder.createConstructScript(abiParams, gasLimit, gasPrice);
        return contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice, getFeePerKb(), fee, mContext);
    }

    @Override
    public BigDecimal getFeePerKb() {
        return new BigDecimal(QtumSharedPreference.getInstance().getFeePerKb(mContext));
    }

    @Override
    public int getMinGasPrice() {
        return QtumSharedPreference.getInstance().getMinGasPrice(mContext);
    }
}
