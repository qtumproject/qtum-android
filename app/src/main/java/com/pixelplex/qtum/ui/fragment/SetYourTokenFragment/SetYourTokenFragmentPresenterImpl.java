package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ContractConfirmFragment.ContractConfirmFragment;
import com.pixelplex.qtum.ui.fragment.TemplatesFragment.TemplatesFragmentInteractorImpl;

import java.util.List;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class SetYourTokenFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetYourTokenFragmentPresenter {

    SetYourTokenFragmentView view;
    Context mContext;
    TemplatesFragmentInteractorImpl interactor;

    ContractMethod contractMethod;

    public SetYourTokenFragmentPresenterImpl(SetYourTokenFragmentView view) {
        this.view = view;
        this.mContext = getView().getContext();
        interactor = new TemplatesFragmentInteractorImpl();
    }

    @Override
    public SetYourTokenFragmentView getView() {
        return view;
    }

    public void getConstructorByName(String name) {
       contractMethod = FileStorageManager.getInstance().getContractConstructor(mContext,name);
       getView().onContractConstructorPrepared(contractMethod.inputParams);
    }

    public void confirm(List<ContractMethodParameter> list, String contractName){
        ContractConfirmFragment fragment = ContractConfirmFragment.newInstance(list, contractName);
        getView().openFragment(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
    }
}
