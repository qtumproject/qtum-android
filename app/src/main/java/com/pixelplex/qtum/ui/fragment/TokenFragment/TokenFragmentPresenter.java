package com.pixelplex.qtum.ui.fragment.TokenFragment;

import android.content.Context;
import android.widget.Toast;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.model.gson.CallSmartContractRequest;
import com.pixelplex.qtum.model.gson.callSmartContractResponse.CallSmartContractResponse;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.sha3.sha.Keccak;
import com.pixelplex.qtum.utils.sha3.sha.Parameters;

import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TokenFragmentPresenter extends BaseFragmentPresenterImpl {

    TokenFragmentView view;
    Context mContext;

    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
        getView().setBalance(this.token.getLastBalance());
        getView().setTokenAddress(this.token.getContractAddress());
        getView().setSenderAddress(this.token.getSenderAddress());
    }

    public TokenFragmentPresenter(TokenFragmentView view){
        this.view = view;
        this.mContext = getView().getContext();
    }

    @Override
    public TokenFragmentView getView() {
        return view;
    }

    public void onRefresh() {
        Toast.makeText(mContext,"Refreshing...", Toast.LENGTH_SHORT).show();
    }

    public void getPropertyValue(final String propName) {

        getContractMethod(token.getUiid(), propName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ContractMethod>() {
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
                            public void onError(Throwable e) {}

                            @Override
                            public void onNext(String[] hashes) {
                                QtumService.newInstance().callSmartContract(token.getContractAddress(), new CallSmartContractRequest(hashes))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<CallSmartContractResponse>() {
                                            @Override
                                            public void onCompleted() {}
                                            @Override
                                            public void onError(Throwable e) {}
                                            @Override
                                            public void onNext(CallSmartContractResponse callSmartContractResponse) {
                                                getView().onContractPropertyUpdated(propName, processResponse(contractMethod.outputParams, callSmartContractResponse.getItems().get(0).getOutput()));
                                            }
                                        });
                            }
                        });
            }
        });

    }

    private String processResponse(List<ContractMethodParameter> contractMethodOutputParameterList, String output){
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

    public Observable<String[]> getHash(final String name) {

        return Observable.fromCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                Keccak keccak = new Keccak();
                String hashMethod = keccak.getHash(Hex.toHexString((name + "()").getBytes()), Parameters.KECCAK_256).substring(0,8);
                return new String[]{hashMethod};
            }
        });
    }


    private Observable<ContractMethod> getContractMethod(final long contractUiid, final String methodName) {

        return Observable.fromCallable(new Callable<ContractMethod>() {
            @Override
            public ContractMethod call() throws Exception {
                List<ContractMethod> methods = FileStorageManager.getInstance().getContractMethods(getView().getContext(), contractUiid);
                for (ContractMethod method: methods) {
                    if(method.name.equals(methodName)){
                        return method;
                    }
                }
                return null;
            }
        });
    }


}

