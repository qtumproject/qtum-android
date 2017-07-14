package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.model.gson.CallSmartContractRequest;
import com.pixelplex.qtum.model.gson.callSmartContractResponse.CallSmartContractResponse;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.sha3.sha.Keccak;
import com.pixelplex.qtum.utils.sha3.sha.Parameters;

import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ContractManagementFragmentPresenter extends BaseFragmentPresenterImpl {

    private ContractManagementFragmentView mContractManagementFragmentView;

    ContractManagementFragmentPresenter(ContractManagementFragmentView contractManagementFragmentView){
        mContractManagementFragmentView = contractManagementFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<ContractMethod> contractMethodList = FileStorageManager.getInstance().getContractMethods(getView().getContext(),getView().getContractTemplateUiid());
        if(contractMethodList != null) {
            getView().setRecyclerView(contractMethodList);
        } else {
            getView().setAlertDialog("Error","Fail to get contract methods", BaseFragment.PopUpType.error);
        }
    }

    private Observable<String[]> getHash(final String name) {

        return Observable.fromCallable(new Callable<String[]>() {
            @Override
            public String[] call() throws Exception {
                Keccak keccak = new Keccak();
                String hashMethod = keccak.getHash(Hex.toHexString((name + "()").getBytes()), Parameters.KECCAK_256).substring(0,8);
                return new String[]{hashMethod};
            }
        });
    }

    public void getPropertyValue(final String contractAddress, final ContractMethod contractMethod, final GetPropertyValueCallBack callBack) {
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
                        QtumService.newInstance().callSmartContract(contractAddress, new CallSmartContractRequest(hashes))
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

    @Override
    public ContractManagementFragmentView getView() {
        return mContractManagementFragmentView;
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

    public interface GetPropertyValueCallBack{
        void onSuccess(String value);
    }
}
