package org.qtum.wallet.ui.fragment.contract_function_fragment.contract_constant_function_fragment;

import android.content.Context;

import org.bitcoinj.script.Script;
import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.CallSmartContractRequest;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.utils.ContractBuilder;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public class ContractFunctionConstantInteractorImpl implements ContractFunctionConstantInteractor {

    private WeakReference<Context> mContext;

    public ContractFunctionConstantInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public List<ContractMethod> getContractMethod(String contractTemplateUiid) {
        return FileStorageManager.getInstance().getContractMethods(mContext.get(), contractTemplateUiid);
    }

    @Override
    public BigDecimal getFeePerKb() {
        QtumSettingSharedPreference qtumSettingSharedPreference = QtumSettingSharedPreference.getInstance();
        return new BigDecimal(qtumSettingSharedPreference.getFeePerKb(mContext.get()));
    }

    @Override
    public int getMinGasPrice() {
        return QtumSharedPreference.getInstance().getMinGasPrice(mContext.get());
    }

    @Override
    public Observable<CallSmartContractRespWrapper> callSmartContractObservable(final String methodName, final List<ContractMethodParameter> contractMethodParameterList, final Contract contract) {
        final CallSmartContractRespWrapper wrapper = new CallSmartContractRespWrapper();
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                ContractBuilder contractBuilder = new ContractBuilder();
                return contractBuilder.createAbiMethodParams(methodName, contractMethodParameterList);
            }
        }).flatMap(new Func1<String, Observable<CallSmartContractResponse>>() {
            @Override
            public Observable<CallSmartContractResponse> call(String s) {
                wrapper.setAbiParams(s);
                return QtumService.newInstance().callSmartContract(contract.getContractAddress(), new CallSmartContractRequest(new String[]{s}, contract.getSenderAddress()));
            }
        }).map(new Func1<CallSmartContractResponse, CallSmartContractRespWrapper>() {
            @Override
            public CallSmartContractRespWrapper call(CallSmartContractResponse response) {
                wrapper.setResponse(response);
                return wrapper;
            }
        });
    }

    @Override
    public String createTransactionHash(String abiParams, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, BigDecimal feePerKb, String fee, final String contractAddress, String sendToContract) {
        ContractBuilder contractBuilder = new ContractBuilder();
        Script script = contractBuilder.createMethodScript(abiParams, gasLimit, gasPrice, contractAddress);

        return contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice, feePerKb, fee, sendToContract,mContext.get());
    }

    @Override
    public Contract getContractByAddress(String address) {
        TinyDB tinyDB = new TinyDB(mContext.get());
        for (Contract contract : tinyDB.getContractList()) {
            if (contract.getContractAddress().equals(address)) {
                return contract;
            }
        }
        return null;
    }

    public static class CallSmartContractRespWrapper {
        private String abiParams;
        private CallSmartContractResponse response;

        public CallSmartContractRespWrapper(String abiParams, CallSmartContractResponse response) {
            this.abiParams = abiParams;
            this.response = response;
        }

        public CallSmartContractRespWrapper() {

        }

        public String getAbiParams() {
            return abiParams;
        }

        public void setAbiParams(String abiParams) {
            this.abiParams = abiParams;
        }

        public CallSmartContractResponse getResponse() {
            return response;
        }

        public void setResponse(CallSmartContractResponse response) {
            this.response = response;
        }
    }
}
