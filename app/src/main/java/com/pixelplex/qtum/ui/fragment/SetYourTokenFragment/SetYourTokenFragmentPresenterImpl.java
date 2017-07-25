package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ContractConfirmFragment.ContractConfirmFragment;
import com.pixelplex.qtum.ui.fragment.TemplatesFragment.TemplatesFragmentInteractorImpl;

import java.util.List;


public class SetYourTokenFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetYourTokenFragmentPresenter {

    private SetYourTokenFragmentView view;
    private Context mContext;
    private TemplatesFragmentInteractorImpl interactor;

    private ContractMethod contractMethod;

    public SetYourTokenFragmentPresenterImpl(SetYourTokenFragmentView view) {
        this.view = view;
        this.mContext = getView().getContext();
        interactor = new TemplatesFragmentInteractorImpl();
    }

    @Override
    public SetYourTokenFragmentView getView() {
        return view;
    }

    public void getConstructorByUiid(String uiid) {
       contractMethod = FileStorageManager.getInstance().getContractConstructor(mContext,uiid);
       getView().onContractConstructorPrepared(contractMethod.inputParams);
    }

    public void confirm(List<ContractMethodParameter> list, String uiid){
        BaseFragment fragment = ContractConfirmFragment.newInstance(getView().getContext(), list, uiid);
        getView().openFragment(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
    }
}
