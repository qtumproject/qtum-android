package org.qtum.wallet.utils;


import android.content.Context;

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
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContractManagementHelper {

    public static void getPropertyValue(final String propName, final Contract contract, Context context, final GetPropertyValueCallBack callBack) {

        getContractMethod(contract.getUiid(), propName,context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ContractMethod>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(final ContractMethod contractMethod) {
                getHash(contractMethod.name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String[]>() {
                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String[] hashes) {
                                QtumService.newInstance().callSmartContract(contract.getContractAddress(), new CallSmartContractRequest(hashes))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<CallSmartContractResponse>() {
                                            @Override
                                            public void onCompleted() {}
                                            @Override
                                            public void onError(Throwable e) {}
                                            @Override
                                            public void onNext(CallSmartContractResponse callSmartContractResponse) {
                                                callBack.onSuccess(processResponse(contractMethod.outputParams, callSmartContractResponse.getItems().get(0).getOutput()));
                                            }
                                        });
                            }
                        });
            }
        });

    }

    public static void getPropertyValue(final Contract contract, final ContractMethod contractMethod, final GetPropertyValueCallBack callBack) {
        getHash(contractMethod.name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String[] hashes) {
                        QtumService.newInstance().callSmartContract(contract.getContractAddress(), new CallSmartContractRequest(hashes, contract.getSenderAddress()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<CallSmartContractResponse>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(CallSmartContractResponse callSmartContractResponse) {
                                        callBack.onSuccess(processResponse(contractMethod.outputParams, callSmartContractResponse.getItems().get(0).getOutput()));
                                    }
                                });
                    }
                });
    }

    private static Observable<String[]> getHash(final String name) {

        return Observable.fromCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                Keccak keccak = new Keccak();
                String hashMethod = keccak.getHash(Hex.toHexString((name + "()").getBytes()), Parameters.KECCAK_256).substring(0,8);
                return new String[]{hashMethod};
            }
        });
    }


    private static Observable<ContractMethod> getContractMethod(final String contractUiid, final String methodName, final Context context) {

        return Observable.fromCallable(new Callable<ContractMethod>() {
            @Override
            public ContractMethod call() throws Exception {
                List<ContractMethod> methods = FileStorageManager.getInstance().getContractMethods(context, contractUiid);
                for (ContractMethod method: methods) {
                    if(method.name.equals(methodName)){
                        return method;
                    }
                }
                return null;
            }
        });
    }

    private static String processResponse(List<ContractMethodParameter> contractMethodOutputParameterList, String output){
        String type = contractMethodOutputParameterList.get(0).getType();
        if(type.contains("int")){
            if(output.isEmpty()){
                return "0";
            }
            return new BigInteger(Hex.decode(output)).toString();
        }else if(type.contains("string")){
            int length = new BigInteger(Hex.decode(output.substring(64,128))).intValue();
            String stringOutput = new String(Hex.decode(output.substring(128,128+length*2)));
            if(stringOutput.isEmpty()){
                stringOutput = "N/A";
            }
            return stringOutput;
        }
        return output;
    }

    public interface GetPropertyValueCallBack{
        void onSuccess(String value);
    }

}
