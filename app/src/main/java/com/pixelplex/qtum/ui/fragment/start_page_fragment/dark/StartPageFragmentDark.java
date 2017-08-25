package com.pixelplex.qtum.ui.fragment.start_page_fragment.dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.start_page_fragment.StartPageFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class StartPageFragmentDark extends StartPageFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_start_page;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        hideBottomNavView(R.color.background);
    }
}
