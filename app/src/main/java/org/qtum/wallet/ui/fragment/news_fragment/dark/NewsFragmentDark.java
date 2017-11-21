package org.qtum.wallet.ui.fragment.news_fragment.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.news_fragment.NewsFragment;

import java.util.List;

public class NewsFragmentDark extends NewsFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_news;
    }

    @Override
    public void initializeViews() {
        ((MainActivity) getActivity()).showBottomNavigationView(org.qtum.wallet.R.color.colorPrimary);
        super.initializeViews();
    }

    @Override
    public void updateNews(List<News> newses) {
        mNewsAdapter = new NewsAdapter(newses, R.layout.item_news);
        mRecyclerView.setAdapter(mNewsAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
