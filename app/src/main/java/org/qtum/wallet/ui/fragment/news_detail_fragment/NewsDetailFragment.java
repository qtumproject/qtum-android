package org.qtum.wallet.ui.fragment.news_detail_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.jsoup.select.Elements;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class NewsDetailFragment extends BaseFragment implements NewsDetailView {

    private NewsDetailPresenter mNewsDetailPresenter;
    private static final String NEWS_POSITION = "news_position";

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_date)
    TextView mTextViewDate;
    @BindView(R.id.tv_title)
    TextView mTextViewTitle;

    public static BaseFragment newInstance(Context context, int newsPosition) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, NewsDetailFragment.class);
        args.putInt(NEWS_POSITION, newsPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
    }

    @Override
    protected void createPresenter() {
        mNewsDetailPresenter = new NewsDetailPresenterImpl(this, new NewsDetailInteractorImpl(getContext()));
    }

    @Override
    protected NewsDetailPresenter getPresenter() {
        return mNewsDetailPresenter;
    }


    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.ibt_back:
                getMainActivity().onBackPressed();
                break;
        }
    }

    public void setUpTitleAndDate(String title, String date){
        mTextViewTitle.setText(title);
        mTextViewDate.setText(date);
    }

    @Override
    public int getNewsPosition() {
        return getArguments().getInt(NEWS_POSITION);
    }
}
