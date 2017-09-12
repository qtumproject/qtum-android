package com.pixelplex.qtum.ui.fragment.smart_contracts_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.profile_fragment.SettingObject;
import com.pixelplex.qtum.ui.fragment.smart_contracts_fragment.SmartContractsFragment;

import java.util.ArrayList;
import java.util.List;


public class SmartContractsFragmentLight extends SmartContractsFragment{

    private List<SettingObject> settingsData;

    @Override
    protected int getLayout() {
        return R.layout.fragment_smart_contracts_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        settingsData = new ArrayList<>();
        settingsData.add(new SettingObject(R.string.my_new_contracts,R.drawable.ic_my_new_contracts_light,0));
        settingsData.add(new SettingObject(R.string.my_published_contracts,R.drawable.ic_my_published_contracts_light,0));
        //settingsData.add(new SettingObject(R.string.contracts_store,R.drawable.qtum_logo,0));
        settingsData.add(new SettingObject(R.string.watch_contract,R.drawable.ic_contr_watch_light,0));
        settingsData.add(new SettingObject(R.string.watch_token,R.drawable.ic_token_watch_light,0));
        settingsData.add(new SettingObject(R.string.backup_contracts,R.drawable.ic_contract_backup_light,0));
        settingsData.add(new SettingObject(R.string.restore_contracts,R.drawable.ic_contract_restore_light,0));
        initializeList(R.layout.lyt_profile_pref_list_item_light,R.drawable.color_primary_divider_light,R.drawable.section_setting_divider_light,settingsData);
    }
}
