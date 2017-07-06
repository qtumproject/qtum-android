package com.pixelplex.qtum.ui.fragment.StartPageFragment.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.StartPageFragment.StartPageFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class StartPageFragmentLight extends StartPageFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_start_page_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        hideBottomNavView(R.color.title_color_light);
    }
}
