package com.pixelplex.qtum.ui.fragment.contract_confirm_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class ContractConfirmFragment extends BaseFragment implements  ContractConfirmView, OnValueClick{

    protected static final String paramsKey = "params";
    private static final String CONTRACT_TEMPLATE_UIID = "mUiid";
    private static final String CONTRACT_NAME = "name";


    public static BaseFragment newInstance(Context context, List<ContractMethodParameter> params, String uiid,String name) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ContractConfirmFragment.class);
        args.putSerializable(paramsKey,(ArrayList)params);
        args.putString(CONTRACT_TEMPLATE_UIID, uiid);
        args.putString(CONTRACT_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    protected ContractConfirmPresenterImpl presenter;
    protected ContractConfirmAdapter adapter;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView confirmList;

    @OnClick(R.id.ibt_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.confirm)
    public void onConfirmClick(){
        presenter.confirmContract(getArguments().getString(CONTRACT_TEMPLATE_UIID));
    }

    @Override
    protected void createPresenter() {
        presenter = new ContractConfirmPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    public String getContractName() {
        return getArguments().getString(CONTRACT_NAME);
    }

    @Override
    public void onClick(int adapterPosition) {
    }

    @Override
    public void makeToast(String s) {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public QtumApplication getApplication() {
        return getMainActivity().getQtumApplication();
    }

}
