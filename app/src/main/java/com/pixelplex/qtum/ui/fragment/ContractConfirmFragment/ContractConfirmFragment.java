package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmFragment extends BaseFragment implements  ContractConfirmView{

    public final int LAYOUT = R.layout.lyt_contract_confirm;
    public static final String paramsKey = "params";

    public static ContractConfirmFragment newInstance(List<ContractMethodParameter> params) {
        Bundle args = new Bundle();
        ContractConfirmFragment fragment = new ContractConfirmFragment();
        args.putSerializable(paramsKey,(ArrayList)params);
        fragment.setArguments(args);
        return fragment;
    }

    ContractConfirmPresenterImpl presenter;
    ContractConfirmAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView confirmList;

    @OnClick({R.id.ibt_back, R.id.cancel})
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.confirm)
    public void onConfirmClick(){

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
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        presenter.setParams((List<ContractMethodParameter>) getArguments().getSerializable(paramsKey));
        confirmList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContractConfirmAdapter(presenter.getParams(),"4jhbr4hjb4l23342i4bn2kl4b2352l342k35bv235rl23","0.100");
        confirmList.setAdapter(adapter);

    }
}
