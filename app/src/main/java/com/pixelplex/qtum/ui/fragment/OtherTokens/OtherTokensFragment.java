package com.pixelplex.qtum.ui.fragment.OtherTokens;

import android.content.Context;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.model.gson.tokenBalance.TokenBalance;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.OtherTokens.Dark.TokensAdapterDark;
import com.pixelplex.qtum.ui.fragment.OtherTokens.Light.TokensAdapterLight;
import com.pixelplex.qtum.utils.ContractManagementHelper;
import com.pixelplex.qtum.utils.FontTextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


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
                            mSwipeRefreshLayout.setRefreshing(false);
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

    private class TokensAdapter extends RecyclerView.Adapter<TokenViewHolder> {

        private final UpdateSocketInstance socketInstace;
        private List<Token> tokens;
        private OnTokenClickListener listener;

        public TokensAdapter(List<Token> tokens, UpdateSocketInstance socketInstance, OnTokenClickListener listener) {
            this.tokens = tokens;
            this.socketInstace = socketInstance;
            this.listener = listener;
        }

        public Contract get(int adapterPosition) {
            return tokens.get(adapterPosition);
        }

        @Override
        public TokenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TokenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_token_list_item, parent, false), socketInstace, listener);
        }

        @Override
        public void onBindViewHolder(TokenViewHolder holder, int position) {
            holder.bind(tokens.get(position));
        }

        public void setTokens(List<Token> tokens) {
            this.tokens = tokens;
        }

        @Override
        public int getItemCount() {
            return tokens.size();
        }
    }

    class TokenViewHolder extends RecyclerView.ViewHolder implements TokenBalanceChangeListener {

        @BindView(R.id.root_layout)
        RelativeLayout rootLayout;

        @BindView(R.id.token_name)
        FontTextView tokenName;

        @BindView(R.id.token_balance)
        FontTextView tokenBalanceView;

        @BindView(R.id.token_symbol)
        FontTextView mTextViewSymbol;

        @BindView(R.id.spinner)
        ProgressBar spinner;

        private Token token;

        private UpdateSocketInstance socketInstance;

        public TokenViewHolder(View itemView, UpdateSocketInstance socketInstance, final OnTokenClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.socketInstance = socketInstance;

            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTokenClick(getAdapterPosition());
                }
            });
        }

        public void bind (Token token) {

            if(this.token != null) {
                socketInstance.getSocketInstance().removeTokenBalanceChangeListener(token.getContractAddress());
            }

            this.token = token;

            ContractManagementHelper.getPropertyValue("symbol", token, getContext(), new ContractManagementHelper.GetPropertyValueCallBack() {
                @Override
                public void onSuccess(String value) {
                    mTextViewSymbol.setVisibility(View.VISIBLE);
                    mTextViewSymbol.setText(value);
                }
            });

            tokenName.setText(token.getContractAddress());
            tokenBalanceView.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            socketInstance.getSocketInstance().addTokenBalanceChangeListener(token.getContractAddress(),this);

        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBalanceChange(final TokenBalance tokenBalance) {
            if(token.getContractAddress().equals(tokenBalance.getContractAddress())){
                token.setLastBalance(tokenBalance.getTotalBalance());
                rootLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.GONE);
                        tokenBalanceView.setText(String.valueOf(tokenBalance.getTotalBalance()));
                        tokenBalanceView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

}