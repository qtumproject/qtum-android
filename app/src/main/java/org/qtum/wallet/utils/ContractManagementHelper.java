package org.qtum.wallet.utils;

import android.content.Context;
import android.util.Pair;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.CallSmartContractRequest;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.utils.sha3.Keccak;
import org.qtum.wallet.utils.sha3.Parameters;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;

public class ContractManagementHelper {

    public static Observable<String> getPropertyValue(final String propName, final Contract contract, final Context context) {

        return Observable.fromCallable(new Callable<Pair<String[], ContractMethod>>() {
            @Override
            public Pair<String[], ContractMethod> call() throws Exception {
                final ContractMethod contractMethod = getContractMethod(contract.getUiid(), propName, context);
                return new Pair<>(getHash(contractMethod.name), contractMethod);
            }
        }).flatMap(new Func1<Pair<String[], ContractMethod>, Observable<String>>() {
            @Override
            public Observable<String> call(final Pair<String[], ContractMethod> pair) {
                return QtumService.newInstance().callSmartContract(contract.getContractAddress(), new CallSmartContractRequest(pair.first))
                        .map(new Func1<CallSmartContractResponse, String>() {
                            @Override
                            public String call(CallSmartContractResponse callSmartContractResponse) {
                                return processResponse(pair.second.outputParams, callSmartContractResponse.getItems().get(0).getOutput());
                            }
                        });
            }
        });
    }

    public static Observable<String> getPropertyValue(final Contract contract, final ContractMethod contractMethod) {

        return Observable.fromCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                return getHash(contractMethod.name);
            }
        }).flatMap(new Func1<String[], Observable<String>>() {
            @Override
            public Observable<String> call(String[] strings) {
                return QtumService.newInstance().callSmartContract(contract.getContractAddress(), new CallSmartContractRequest(strings, contract.getSenderAddress()))
                        .map(new Func1<CallSmartContractResponse, String>() {
                            @Override
                            public String call(CallSmartContractResponse callSmartContractResponse) {
                                return processResponse(contractMethod.outputParams, callSmartContractResponse.getItems().get(0).getOutput());
                            }
                        });
            }
        });
    }

    private static String[] getHash(final String name) {
        Keccak keccak = new Keccak();
        String hashMethod = keccak.getHash(Hex.toHexString((name + "()").getBytes()), Parameters.KECCAK_256).substring(0, 8);
        return new String[]{hashMethod};
    }


    private static ContractMethod getContractMethod(final String contractUiid, final String methodName, final Context context) {
        List<ContractMethod> methods = FileStorageManager.getInstance().getContractMethods(context, contractUiid);
        for (ContractMethod method : methods) {
            if (method.name.equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    private static String processResponse(List<ContractMethodParameter> contractMethodOutputParameterList, String output) {
        String type = contractMethodOutputParameterList.get(0).getType();
        if (type.contains("int")) {
            if (output.isEmpty()) {
                return "0";
            }
            return new BigInteger(Hex.decode(output)).toString();
        } else if (type.contains("string")) {
            int length = new BigInteger(Hex.decode(output.substring(64, 128))).intValue();
            String stringOutput = new String(Hex.decode(output.substring(128, 128 + length * 2)));
            if (stringOutput.isEmpty()) {
                stringOutput = "N/A";
            }
            return stringOutput;
        }
        return output;
    }

    public interface GetPropertyValueCallBack {
        void onSuccess(String value);
    }
}
