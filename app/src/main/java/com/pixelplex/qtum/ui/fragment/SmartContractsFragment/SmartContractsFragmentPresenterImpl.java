package com.pixelplex.qtum.ui.fragment.SmartContractsFragment;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.MyContractsFragment.MyContractsFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.SettingObject;
import com.pixelplex.qtum.ui.fragment.SmartContractListFragment.SmartContractListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max-v on 5/31/2017.
 */

public class SmartContractsFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SmartContractsFragmentPresenter {

    SmartContractsFragmentView view;

    List<SettingObject> settingsData;

    SmartContractsFragmentPresenterImpl(SmartContractsFragmentView smartContractsFragmentView){
        view = smartContractsFragmentView;
        initSettingsData();
    }

    private void initSettingsData() {
        settingsData = new ArrayList<>();
        settingsData.add(new SettingObject(R.string.create_contract,R.drawable.ic_my_new_contracts,0));
        settingsData.add(new SettingObject(R.string.my_contracts,R.drawable.ic_my_publiched_contracts,0));
        settingsData.add(new SettingObject(R.string.contracts_store,R.drawable.ic_contract_store,0));
    }

    public List<SettingObject> getSettingsData () {
        return settingsData;
    }

    public void onCreateContractClick(){
        SmartContractListFragment smartContractListFragment = SmartContractListFragment.newInstance();
        view.openFragment(smartContractListFragment);
    }

    public void onMyContractsClick(){
        MyContractsFragment myContractsFragment = MyContractsFragment.newInstance();
        view.openFragment(myContractsFragment);
    }

    public void onContractsStoreClick(){

    }

    @Override
    public SmartContractsFragmentView getView() {
        return view;
    }
}
