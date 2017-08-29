package com.pixelplex.qtum.ui.fragment.news_fragment.dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.news_fragment.NewsFragment;

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
