package com.pixelplex.qtum.ui.fragment.SmartContractConstructorFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class SmartContractConstructorFragment extends BaseFragment implements SmartContractConstructorView {

    public final int LAYOUT = R.layout.lyt_smart_contract_constructor;
    public static String constructorNameKey = "constructorName";

    private ConstructorAdapter adapter;

    public static SmartContractConstructorFragment newInstance(String contractName) {
        Bundle args = new Bundle();
        SmartContractConstructorFragment fragment = new SmartContractConstructorFragment();
        args.putString(constructorNameKey,contractName);
        fragment.setArguments(args);
        return fragment;
    }

    SmartContractConstructorPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView constructorList;

    @OnClick({R.id.ibt_back, R.id.cancel})
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.confirm)
    public void onConfirmClick(){
        if(adapter != null) {
            presenter.confirm(adapter.getParams());
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SmartContractConstructorPresenterImpl(this);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        constructorList.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.getConstructorByName(getArguments().getString(constructorNameKey));
    }

    @Override
    public void onContractConstructorPrepared(List<ContractMethodParameter> params) {
        adapter = new ConstructorAdapter(params);
        constructorList.setAdapter(adapter);
    }
}
