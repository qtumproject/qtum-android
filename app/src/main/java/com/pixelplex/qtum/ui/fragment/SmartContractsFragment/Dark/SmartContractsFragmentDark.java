package com.pixelplex.qtum.ui.fragment.SmartContractsFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.SettingObject;
import com.pixelplex.qtum.ui.fragment.SmartContractsFragment.SmartContractsFragment;

import java.util.ArrayList;
import java.util.List;


public class SmartContractsFragmentDark extends SmartContractsFragment {

    private List<SettingObject> settingsData;

    @Override
    protected int getLayout() {
        return R.layout.fragment_smart_contracts;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        settingsData = new ArrayList<>();
        settingsData.add(new SettingObject(R.string.my_new_contracts,R.drawable.ic_my_new_contracts,0));
        settingsData.add(new SettingObject(R.string.my_published_contracts,R.drawable.ic_my_published_contracts,0));
        settingsData.add(new SettingObject(R.string.contracts_store,R.drawable.qtum_logo,0));
        settingsData.add(new SettingObject(R.string.watch_contract,R.drawable.ic_contr_watch,0));
        settingsData.add(new SettingObject(R.string.watch_token,R.drawable.ic_token_watch,0));
        settingsData.add(new SettingObject(R.string.restore_contracts,R.drawable.ic_contract_restore,0));
        settingsData.add(new SettingObject(R.string.backup_contracts,R.drawable.ic_contract_backup,0));

        initializeList(R.layout.lyt_profile_pref_list_item,R.drawable.color_primary_divider, R.drawable.section_setting_divider,settingsData);
    }
}
