package org.qtum.wallet.ui.fragment.news_fragment.dark;

import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.news_fragment.NewsFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class NewsFragmentDark extends NewsFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_news;
    }

    @Override
    public void initializeViews() {
        ((MainActivity)getActivity()).showBottomNavigationView(org.qtum.wallet.R.color.colorPrimary);
        super.initializeViews();
    }
}
