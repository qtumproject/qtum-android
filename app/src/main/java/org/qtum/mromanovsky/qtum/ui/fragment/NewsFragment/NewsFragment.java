package org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;


public class NewsFragment extends BaseFragment implements NewsFragmentView{

    public static final int  LAYOUT = R.layout.fragment_news;

    NewsFragmentPresenterImpl mNewsFragmentPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static NewsFragment newInstance(){
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
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
