package org.qtum.wallet.ui.fragment.news_detail_fragment;

import android.content.Context;
import android.os.Bundle;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;


public abstract class NewsDetailFragment extends BaseFragment implements NewsDetailView{

    private NewsDetailPresenter mNewsDetailPresenter;
    private static final String NEWS_POSITION = "news_position";

    public static BaseFragment newInstance(Context context, int newsPosition) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context,NewsDetailFragment.class) ;
        args.putInt(NEWS_POSITION, newsPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mNewsDetailPresenter = new NewsDetailPresenterImpl(this, new NewsDetailInteractorImpl(getContext()));
    }

    @Override
    protected NewsDetailPresenter getPresenter() {
        return mNewsDetailPresenter;
    }

    @Override
    public int getNewsPosition() {
        return getArguments().getInt(NEWS_POSITION);
    }
}
