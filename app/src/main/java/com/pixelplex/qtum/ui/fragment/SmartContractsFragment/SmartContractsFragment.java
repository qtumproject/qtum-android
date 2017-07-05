package com.pixelplex.qtum.ui.fragment.SmartContractsFragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.DividerItemDecoration;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.OnSettingClickListener;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.PrefAdapter;

import butterknife.BindView;
import butterknife.OnClick;


public class SmartContractsFragment extends BaseFragment implements OnSettingClickListener, SmartContractsFragmentView{

    private SmartContractsFragmentPresenterImpl presenter;

    @BindView(R.id.smart_contracts_list)
    RecyclerView smartContractsList;

    @OnClick(R.id.ibt_back)

    public void onClick() {

        getActivity().onBackPressed();
    }

    private PrefAdapter adapter;

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
                getPresenter().onCreateContractClick();
                break;
            case R.string.my_contracts:
                getPresenter().onMyContractsClick();
                break;
            case R.string.contracts_store:
                getPresenter().onContractsStoreClick();
                break;
            case R.string.watch_contract:
                getPresenter().onWatchContractClick();
                break;
            case R.string.watch_token:
                getPresenter().onWatchTokenClick();
                break;
            case R.string.restore_contracts:
                getPresenter().onRestoreContractsClick();
                break;
            case R.string.backup_contracts:
                getPresenter().onBackupContractsClick();
                break;

        }
    }

    @Override
    public void onSwitchChange(int key, boolean isChecked) {

    }


    @Override
    protected void createPresenter() {
        presenter = new SmartContractsFragmentPresenterImpl(this);
    }

    @Override
    protected SmartContractsFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_smart_contracts;
    }
}
