package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmFragment extends BaseFragment implements  ContractConfirmView, OnValueClick{

    public final int LAYOUT = R.layout.lyt_contract_confirm;
    public static final String paramsKey = "params";
    public static final String contractKey = "contract";

    public static ContractConfirmFragment newInstance(List<ContractMethodParameter> params, String contractName) {
        Bundle args = new Bundle();
        ContractConfirmFragment fragment = new ContractConfirmFragment();
        args.putSerializable(paramsKey,(ArrayList)params);
        args.putString(contractKey, contractName);
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
        presenter.confirmContract(getArguments().getString(contractKey));
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
        presenter.setContractMethodParameterList((List<ContractMethodParameter>) getArguments().getSerializable(paramsKey));
        confirmList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContractConfirmAdapter(presenter.getContractMethodParameterList(),"4jhbr4hjb4l23342i4bn2kl4b2352l342k35bv235rl23","0.100", this);
        confirmList.setAdapter(adapter);

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
        return ((MainActivity)getFragmentActivity()).getQtumApplication();
    }

}
