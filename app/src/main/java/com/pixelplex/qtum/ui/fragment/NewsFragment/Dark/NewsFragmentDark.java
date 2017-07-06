package com.pixelplex.qtum.ui.fragment.NewsFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.NewsFragment.NewsFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class NewsFragmentDark extends NewsFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void initializeViews() {
        ((MainActivity)getActivity()).showBottomNavigationView(R.color.colorPrimary);
        super.initializeViews();
    }
}
