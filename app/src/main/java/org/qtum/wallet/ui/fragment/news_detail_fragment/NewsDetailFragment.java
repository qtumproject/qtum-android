package org.qtum.wallet.ui.fragment.news_detail_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class NewsDetailFragment extends BaseFragment implements NewsDetailView{

    private NewsDetailPresenter mNewsDetailPresenter;
    private static final String NEWS_POSITION = "news_position";

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    public static BaseFragment newInstance(Context context, int newsPosition) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context,NewsDetailFragment.class) ;
        args.putInt(NEWS_POSITION, newsPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public int getNewsPosition() {
        return getArguments().getInt(NEWS_POSITION);
    }

}
