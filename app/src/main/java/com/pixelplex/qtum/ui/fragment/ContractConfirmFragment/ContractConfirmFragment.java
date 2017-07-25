package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class ContractConfirmFragment extends BaseFragment implements  ContractConfirmView, OnValueClick{

    protected static final String paramsKey = "params";
    private static final String CONTRACT_TEMPLATE_UIID = "uiid";

    public static BaseFragment newInstance(Context context, List<ContractMethodParameter> params, String uiid) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ContractConfirmFragment.class);
        args.putSerializable(paramsKey,(ArrayList)params);
        args.putString(CONTRACT_TEMPLATE_UIID, uiid);
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
