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

    public void getAbiFromFile(){
        List<ContractMethod> contractMethodList = FileStorageManager.getInstance().getContractMethods(getView().getContext(),getView().getContractTemplateUiid());
        if(contractMethodList != null) {
            getView().setRecyclerView(contractMethodList, true);
        } else {
            getView().setAlertDialog("Error","Fail to get contract methods", BaseFragment.PopUpType.error);
        }
    }

    public void getAbiFromString(String abi){
        List<ContractMethod> contractMethodList = FileStorageManager.getInstance().getContractMethodsByAbiString(getView().getContext(), abi);
        if(contractMethodList != null) {
            getView().setRecyclerView(contractMethodList, false);
        } else {
            getView().setAlertDialog("Error","Fail to get contract methods", BaseFragment.PopUpType.error);
        }
    }


    @Override
    public ContractManagementFragmentView getView() {
        return mContractManagementFragmentView;
    }

}
