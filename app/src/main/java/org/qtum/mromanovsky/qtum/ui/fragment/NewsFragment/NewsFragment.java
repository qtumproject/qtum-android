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


public class NewsFragment extends BaseFragment implements NewsFragmentView{

    public static final int  LAYOUT = R.layout.fragment_news;

    NewsFragmentPresenterImpl mNewsFragmentPresenter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (null != toolbar) {
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setTitle(R.string.news);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
