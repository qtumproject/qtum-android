package com.pixelplex.qtum.ui.fragment.ProfileFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.DividerItemDecoration;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.Light.PrefAdapterLight;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.ProfileFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.ProfileFragmentPresenterImpl;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class ProfileFragmentDark extends ProfileFragment {

    private PrefAdapterLight adapter;

    @Override
    protected int getLayout() {
        return R.layout.lyt_profile_preference;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        dividerItemDecoration = new DividerItemDecoration(getContext(), R.drawable.color_primary_divider, R.drawable.section_setting_divider, getPresenter().getSettingsData());
        showBottomNavView(R.color.colorPrimary);
        adapter = new PrefAdapterLight(getPresenter().getSettingsData(), this, R.layout.lyt_profile_pref_list_item);
        prefList.addItemDecoration(dividerItemDecoration);
        prefList.setAdapter(adapter);
    }

    @Override
    public void resetText() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        //prefList.removeItemDecoration(dividerItemDecoration);
        super.onDestroyView();
    }
}
