package com.pixelplex.qtum.ui.fragment.ProfileFragment.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.DividerItemDecoration;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.ProfileFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.ProfileFragmentPresenterImpl;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class ProfileFragmentLight extends ProfileFragment {

    @Override
    protected void createPresenter() {
        super.createPresenter();
    }

    @Override
    protected ProfileFragmentPresenterImpl getPresenter() {
        return super.getPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.lyt_profile_preference_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        initializeList(R.layout.lyt_profile_pref_list_item_light,R.drawable.color_primary_divider_light,R.drawable.section_setting_divider_light);
    }
}
