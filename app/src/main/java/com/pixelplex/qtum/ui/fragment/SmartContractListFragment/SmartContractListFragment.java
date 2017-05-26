package com.pixelplex.qtum.ui.fragment.SmartContractListFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.SmartContractsManager.StorageManager;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class SmartContractListFragment extends BaseFragment implements SmartContractListView, ContractSelectListener {

    public final int LAYOUT = R.layout.lyt_contract_list;

    public static SmartContractListFragment newInstance() {
        Bundle args = new Bundle();
        SmartContractListFragment fragment = new SmartContractListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    SmartContractListPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView contractList;

    @OnClick(R.id.ibt_back)
    public void onClick() {
        getActivity().onBackPressed();
    }

    @Override
    protected void createPresenter() {
        presenter = new SmartContractListPresenterImpl(this);
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
        contractList.setLayoutManager(new LinearLayoutManager(getContext()));
        contractList.setAdapter(new ContractsRecyclerAdapter(StorageManager.getInstance().getContracts(getContext()),this));
    }

    @Override
    public void onSelectContract(String contractName) {
        presenter.openConstructorByName(contractName);
    }
}
