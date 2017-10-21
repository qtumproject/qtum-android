package org.qtum.wallet.ui.fragment.news_detail_fragment.dark;

import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.news_detail_fragment.NewsDetailFragment;


public class NewsDetailFragmentDark extends NewsDetailFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_news_detail;
    }

    @Override
    public void initializeViews() {
        ((MainActivity)getActivity()).showBottomNavigationView(org.qtum.wallet.R.color.colorPrimary);
        super.initializeViews();
    }
}
