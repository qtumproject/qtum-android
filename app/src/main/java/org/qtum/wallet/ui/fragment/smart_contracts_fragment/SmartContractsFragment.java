package org.qtum.wallet.ui.fragment.smart_contracts_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.profile_fragment.DividerItemDecoration;
import org.qtum.wallet.ui.fragment.profile_fragment.OnSettingClickListener;
import org.qtum.wallet.ui.fragment.profile_fragment.PrefAdapter;
import org.qtum.wallet.ui.fragment.profile_fragment.SettingObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class SmartContractsFragment extends BaseFragment implements OnSettingClickListener, SmartContractsFragmentView{

    private SmartContractsFragmentPresenterImpl presenter;

    @BindView(R.id.smart_contracts_list)
    RecyclerView smartContractsList;

    @OnClick(R.id.ibt_back)

    public void onClick() {

        getActivity().onBackPressed();
    }

    private PrefAdapter adapter;

    public static BaseFragment newInstance(Context context) {
        
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, SmartContractsFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    protected void initializeList(int resId, int resDividerDecoration, int resSectionDecoration, List<SettingObject> settingObjectList){
        smartContractsList.setLayoutManager(new LinearLayoutManager(getContext()));
        smartContractsList.addItemDecoration(new DividerItemDecoration(getContext(), resDividerDecoration,resSectionDecoration, settingObjectList));
        adapter = new PrefAdapter(settingObjectList, this, resId);
        smartContractsList.setAdapter(adapter);
    }

    @Override
    public void onSettingClick(int key) {
        switch (key){
            case R.string.my_new_contracts:
                getPresenter().onCreateContractClick();
                break;
            case R.string.my_published_contracts:
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

}
