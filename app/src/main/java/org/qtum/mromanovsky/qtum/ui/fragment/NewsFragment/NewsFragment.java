package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;


public class NewsFragment extends BaseFragment implements NewsFragmentView {

    public static final int LAYOUT = R.layout.fragment_news;

    NewsFragmentPresenterImpl mNewsFragmentPresenter;

    public static NewsFragment newInstance() {
        NewsFragment newsFragment = new NewsFragment();
        return newsFragment;
    }

    @Override
    protected void createPresenter() {
        mNewsFragmentPresenter = new NewsFragmentPresenterImpl(this);
    }

    @Override
    protected NewsFragmentPresenterImpl getPresenter() {
        return mNewsFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {

    }
}
