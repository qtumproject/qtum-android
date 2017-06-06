package com.pixelplex.qtum.ui.fragment.SmartContractsFragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.DividerItemDecoration;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.OnSettingClickListener;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.PrefAdapter;

import butterknife.BindView;
import butterknife.OnClick;


public class SmartContractsFragment extends BaseFragment implements OnSettingClickListener, SmartContractsFragmentView{

    SmartContractsFragmentPresenterImpl presenter;

    @BindView(R.id.smart_contracts_list)
    RecyclerView smartContractsList;

    @OnClick(R.id.ibt_back)
    public void onClick() {
        getActivity().onBackPressed();
    }

    PrefAdapter adapter;

    public static SmartContractsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SmartContractsFragment fragment = new SmartContractsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        smartContractsList.setLayoutManager(new LinearLayoutManager(getContext()));
        smartContractsList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.color_primary_divider, R.drawable.section_setting_divider, presenter.getSettingsData()));
        adapter = new PrefAdapter(presenter.getSettingsData(), this);
        smartContractsList.setAdapter(adapter);
    }

    @Override
    public void onSettingClick(int key) {
        switch (key){
            case R.string.create_contract:
                presenter.onCreateContractClick();
                break;
            case R.string.my_contracts:
                presenter.onMyContractsClick();
                break;
            case R.string.contracts_store:
                presenter.onContractsStoreClick();
                break;

        }
    }


    @Override
    protected void createPresenter() {
        presenter = new SmartContractsFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_smart_contracts;
    }
}
