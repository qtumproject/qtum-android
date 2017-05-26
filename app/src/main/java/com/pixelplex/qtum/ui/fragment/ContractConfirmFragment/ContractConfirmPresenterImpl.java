package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.content.Context;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.List;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmPresenterImpl extends BaseFragmentPresenterImpl implements ContractConfirmPresenter {

    ContractConfirmView view;
    ContractConfirmInteractorImpl interactor;
    Context mContext;

    private List<ContractMethodParameter> params;

    public void setParams(List<ContractMethodParameter> params) {
        this.params = params;
    }

    public List<ContractMethodParameter> getParams() {
        return params;
    }

    public ContractConfirmPresenterImpl(ContractConfirmView view) {
        this.view = view;
        mContext = getView().getContext();
        interactor = new ContractConfirmInteractorImpl();
    }

    @Override
    public ContractConfirmView getView() {
        return view;
    }
}
