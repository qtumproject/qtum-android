package org.qtum.wallet.ui.fragment.profile_fragment.light;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.profile_fragment.DividerItemDecoration;
import org.qtum.wallet.ui.fragment.profile_fragment.PrefAdapter;
import org.qtum.wallet.ui.fragment.profile_fragment.ProfileFragment;

public class ProfileFragmentLight extends ProfileFragment {

    @Override
    protected int getLayout() {
        return R.layout.lyt_profile_preference_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        showBottomNavView(R.color.title_color_light);
        adapter = new PrefAdapterLight(getPresenter().getSettingsData(), this, R.layout.lyt_profile_pref_list_item_light);
        dividerItemDecoration = new DividerItemDecoration(getContext(), R.drawable.color_primary_divider_light, R.drawable.section_setting_divider_light, getPresenter().getSettingsData());
        prefList.addItemDecoration(dividerItemDecoration);
        prefList.setAdapter(adapter);
    }

}
