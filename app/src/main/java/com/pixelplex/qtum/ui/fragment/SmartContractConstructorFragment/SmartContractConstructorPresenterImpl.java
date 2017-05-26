package com.pixelplex.qtum.ui.fragment.SmartContractConstructorFragment;

import android.content.Context;

import com.pixelplex.qtum.SmartContractsManager.StorageManager;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ContractConfirmFragment.ContractConfirmFragment;
import com.pixelplex.qtum.ui.fragment.SmartContractListFragment.SmartContractListInteractorImpl;

import java.util.List;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class SmartContractConstructorPresenterImpl extends BaseFragmentPresenterImpl implements SmartContractConstructorPresenter {

    SmartContractConstructorView view;
    Context mContext;
    SmartContractListInteractorImpl interactor;

    ContractMethod contractMethod;

    public SmartContractConstructorPresenterImpl(SmartContractConstructorView view) {
        this.view = view;
        this.mContext = getView().getContext();
        interactor = new SmartContractListInteractorImpl();
    }

    @Override
    public SmartContractConstructorView getView() {
        return view;
    }

    public void getConstructorByName(String name) {
       contractMethod = StorageManager.getInstance().getContractConstructor(mContext,name);
       getView().onContractConstructorPrepared(contractMethod.inputParams);
    }

    public void confirm(List<ContractMethodParameter> list){
        ContractConfirmFragment fragment = ContractConfirmFragment.newInstance(list);
        getView().openFragment(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
    }
}
