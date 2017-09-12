package org.qtum.wallet.ui.fragment.other_tokens;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;


public abstract class OtherTokensFragment extends BaseFragment implements OtherTokensView, OnTokenClickListener {

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, OtherTokensFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    protected OtherTokensPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    protected
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
                            if (mSwipeRefreshLayout != null) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }, 500);
                }
            }
        });
    }

    @Override
    public void updateTokensData(List<Token> tokensData) {
        if (tokensList.getAdapter() != null) {
            ((TokensAdapter) tokensList.getAdapter()).setTokens(tokensData);
            tokensList.getAdapter().notifyDataSetChanged();
        }
    }
}