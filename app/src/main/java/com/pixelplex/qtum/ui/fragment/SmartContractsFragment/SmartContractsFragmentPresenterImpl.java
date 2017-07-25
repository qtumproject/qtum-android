package com.pixelplex.qtum.ui.fragment.SmartContractsFragment;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BackupContractsFragment.BackupContractsFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.MyContractsFragment.MyContractsFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.SettingObject;
import com.pixelplex.qtum.ui.fragment.QStore.QStoreFragment;
import com.pixelplex.qtum.ui.fragment.RestoreContractsFragment.RestoreContractsFragment;
import com.pixelplex.qtum.ui.fragment.TemplatesFragment.TemplatesFragment;
import com.pixelplex.qtum.ui.fragment.WatchContractFragment.WatchContractFragment;

import java.util.ArrayList;
import java.util.List;


class SmartContractsFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SmartContractsFragmentPresenter {

    private SmartContractsFragmentView mSmartContractsFragmentView;

    SmartContractsFragmentPresenterImpl(SmartContractsFragmentView smartContractsFragmentView){
        mSmartContractsFragmentView = smartContractsFragmentView;
    }

    void onCreateContractClick(){
        BaseFragment smartContractListFragment = TemplatesFragment.newInstance(getView().getContext());
        getView().openFragment(smartContractListFragment);
    }

    void onMyContractsClick(){
        BaseFragment myContractsFragment = MyContractsFragment.newInstance(getView().getContext());
        getView().openFragment(myContractsFragment);
    }

    void onContractsStoreClick(){
        QStoreFragment qStroreFragment = QStoreFragment.newInstance();
        getView().openFragment(qStroreFragment);
    }

    void onWatchContractClick(){
        BaseFragment watchContractFragment = WatchContractFragment.newInstance(getView().getContext(), false);
        getView().openFragment(watchContractFragment);
    }

    void onWatchTokenClick(){
        BaseFragment watchContractFragment = WatchContractFragment.newInstance(getView().getContext(), true);
        getView().openFragment(watchContractFragment);
    }

    void onRestoreContractsClick(){
        BaseFragment restoreContractFragment = RestoreContractsFragment.newInstance(getView().getContext());
        getView().openFragment(restoreContractFragment);
    }

    void onBackupContractsClick(){
        BaseFragment backupContractsFragment = BackupContractsFragment.newInstance(getView().getContext());
        getView().openFragment(backupContractsFragment);
    }

    @Override
    public SmartContractsFragmentView getView() {
        return mSmartContractsFragmentView;
    }
}
