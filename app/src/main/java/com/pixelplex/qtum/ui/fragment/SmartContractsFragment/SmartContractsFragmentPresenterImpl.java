package com.pixelplex.qtum.ui.fragment.SmartContractsFragment;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.MyContractsFragment.MyContractsFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.SettingObject;
import com.pixelplex.qtum.ui.fragment.RestoreContractsFragment.RestoreContractsFragment;
import com.pixelplex.qtum.ui.fragment.SmartContractListFragment.SmartContractListFragment;
import com.pixelplex.qtum.ui.fragment.WatchContractFragment.WatchContractFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max-v on 5/31/2017.
 */

public class SmartContractsFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SmartContractsFragmentPresenter {

    SmartContractsFragmentView mSmartContractsFragmentView;

    List<SettingObject> settingsData;

    SmartContractsFragmentPresenterImpl(SmartContractsFragmentView smartContractsFragmentView){
        mSmartContractsFragmentView = smartContractsFragmentView;
        initSettingsData();
    }

    private void initSettingsData() {
        settingsData = new ArrayList<>();
        settingsData.add(new SettingObject(R.string.create_contract,R.drawable.ic_my_new_contracts,0));
        settingsData.add(new SettingObject(R.string.my_contracts,R.drawable.ic_my_publiched_contracts,0));
        settingsData.add(new SettingObject(R.string.contracts_store,R.drawable.ic_contract_store,0));
        settingsData.add(new SettingObject(R.string.watch_contract,R.drawable.ic_contr_watch,0));
        settingsData.add(new SettingObject(R.string.watch_token,R.drawable.ic_token_watch,0));
        settingsData.add(new SettingObject(R.string.restore_contracts,R.drawable.ic_contract_restore,0));
        settingsData.add(new SettingObject(R.string.backup_contracts,R.drawable.ic_contr_backup,0));
    }

    public List<SettingObject> getSettingsData () {
        return settingsData;
    }

    public void onCreateContractClick(){
        SmartContractListFragment smartContractListFragment = SmartContractListFragment.newInstance();
        getView().openFragment(smartContractListFragment);
    }

    public void onMyContractsClick(){
        MyContractsFragment myContractsFragment = MyContractsFragment.newInstance();
        getView().openFragment(myContractsFragment);
    }

    public void onContractsStoreClick(){

    }

    public void onWatchContractClick(){
        WatchContractFragment watchContractFragment = WatchContractFragment.newInstance(false);
        getView().openFragment(watchContractFragment);
    }

    public void onWatchTokenClick(){
        WatchContractFragment watchContractFragment = WatchContractFragment.newInstance(true);
        getView().openFragment(watchContractFragment);
    }

    public void onRestoreContractsClick(){
        RestoreContractsFragment restoreContractFragment = RestoreContractsFragment.newInstance();
        getView().openFragment(restoreContractFragment);
    }

    public void onBackupContractsClick(){

    }

    @Override
    public SmartContractsFragmentView getView() {
        return mSmartContractsFragmentView;
    }
}
