package org.qtum.wallet.ui.fragment.news_detail_fragment.light;

import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.news_detail_fragment.NewsDetailFragment;


public class NewsDetailFragmentLight extends NewsDetailFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_news_detail_light;
    }

    @Override
    public void initializeViews() {
        ((MainActivity)getActivity()).showBottomNavigationView(org.qtum.wallet.R.color.title_color_light);
        super.initializeViews();
    }

}
