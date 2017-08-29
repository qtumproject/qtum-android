package com.pixelplex.qtum.ui.fragment.set_your_token_fragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.contract_confirm_fragment.ContractConfirmFragment;
import com.pixelplex.qtum.ui.fragment.templates_fragment.TemplatesFragmentInteractorImpl;

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

    public void confirm(List<ContractMethodParameter> list, String uiid,String name){
        BaseFragment fragment = ContractConfirmFragment.newInstance(getView().getContext(), list, uiid, name);

        getView().openFragment(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
    }
}