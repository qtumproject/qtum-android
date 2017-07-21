package com.pixelplex.qtum.ui.fragment.OtherTokens;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class OtherTokensFragment extends BaseFragment implements OtherTokensView, OnTokenClickListener {

    private final int LAYOUT = R.layout.fragment_other_tokens;

    public static OtherTokensFragment newInstance() {
        Bundle args = new Bundle();
        OtherTokensFragment fragment = new OtherTokensFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private OtherTokensPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView tokensList;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public void notifyTokenChange() {
        getPresenter().notifyNewToken();
    }

    @Override
    protected void createPresenter() {
        presenter = new OtherTokensPresenterImpl(this);
    }

    @Override
    protected OtherTokensPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        tokensList.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.setTokenList();

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (tokensList.getAdapter() != null) {
                    tokensList.getAdapter().notifyDataSetChanged();
                    mSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 500);
                }
            }
        });
    }

    @Override
    public void setTokensData(List<Token> tokensData) {
        tokensList.setAdapter(new TokensAdapter(tokensData, presenter, this));
    }

    @Override
    public void updateTokensData(List<Token> tokensData) {
        if (tokensList.getAdapter() != null) {
            ((TokensAdapter) tokensList.getAdapter()).setTokens(tokensData);
            tokensList.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onTokenClick(int adapterPosition) {
        if (tokensList.getAdapter() != null) {
            presenter.openTokenDetails(((TokensAdapter) tokensList.getAdapter()).get(adapterPosition));
        }
    }
}
