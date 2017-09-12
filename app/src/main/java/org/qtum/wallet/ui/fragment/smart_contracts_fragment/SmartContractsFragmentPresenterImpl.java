package org.qtum.wallet.ui.fragment.smart_contracts_fragment;

import org.qtum.wallet.ui.fragment.backup_contracts_fragment.BackupContractsFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.my_contracts_fragment.MyContractsFragment;
import org.qtum.wallet.ui.fragment.qstore.QStoreFragment;
import org.qtum.wallet.ui.fragment.restore_contracts_fragment.RestoreContractsFragment;
import org.qtum.wallet.ui.fragment.templates_fragment.TemplatesFragment;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.WatchContractFragment;


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
        BaseFragment qStroreFragment = QStoreFragment.newInstance(getView().getContext());
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
