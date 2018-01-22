package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.SearchBar;
import org.qtum.wallet.utils.SearchBarListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class SubscribeTokensFragment extends BaseFragment implements SubscribeTokensView, SearchBarListener {

    private AddressesListTokenPresenter mSubscribeTokensPresenterImpl;
    protected TokenAdapter mTokenAdapter;
    private String mSearchString;
    protected List<Token> mCurrentList;

    @BindView(org.qtum.wallet.R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    @BindView(org.qtum.wallet.R.id.tv_currency_title)
    TextView mTextViewCurrencyTitle;

    @BindView(org.qtum.wallet.R.id.search_bar)
    SearchBar searchBar;

    @BindView(org.qtum.wallet.R.id.ll_currency)
    RelativeLayout mFrameLayoutBase;

    @BindView(R.id.place_holder)
    FontTextView mFontTextViewPlaceHolder;

    @OnClick({org.qtum.wallet.R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, SubscribeTokensFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mSubscribeTokensPresenterImpl = new SubscribeTokensPresenterImpl(this, new SubscribeTokensInteractorImpl(getContext()));
    }

    @Override
    protected AddressesListTokenPresenter getPresenter() {
        return mSubscribeTokensPresenterImpl;
    }


    @Override
    public void setPlaceHolder() {
        mFontTextViewPlaceHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
        mTextViewCurrencyTitle.setText(org.qtum.wallet.R.string.chose_to_subscribe);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFrameLayoutBase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    hideKeyBoard();
            }
        });
    }


    @Override
    public void onActivate() {
    }

    @Override
    public void onDeactivate() {
        if (mFrameLayoutBase != null) {
            mFrameLayoutBase.requestFocus();
        }
        hideKeyBoard();
    }

    @Override
    public void onRequestSearch(String filter) {
        if (mTokenAdapter != null) {
            if (filter.isEmpty()) {
                mTokenAdapter.setFilter(mCurrentList);
            } else {
                mSearchString = filter.toLowerCase();
                List<Token> newList = new ArrayList<>();
                for (Token currency : mCurrentList) {
                    if (currency.getContractName().toLowerCase().contains(mSearchString))
                        newList.add(currency);
                }
                final int searchStringSize = mSearchString.length();
                Collections.sort(newList, new Comparator<Token>() {
                    @Override
                    public int compare(Token token, Token token2) {
                        if (token.getContractName().substring(0, searchStringSize).equals(mSearchString) && !token2.getContractName().substring(0, searchStringSize).equals(mSearchString)) {
                            return -1;
                        } else if (!token.getContractName().substring(0, searchStringSize).equals(mSearchString) && token2.getContractName().substring(0, searchStringSize).equals(mSearchString)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                mTokenAdapter.setFilter(newList);
            }
        }
    }

    class TokenHolder extends RecyclerView.ViewHolder {

        @BindView(org.qtum.wallet.R.id.tv_single_string)
        TextView mTextViewCurrency;
        @BindView(org.qtum.wallet.R.id.iv_check_indicator_blue)
        ImageView mImageViewCheckIndicator;
        @BindView(org.qtum.wallet.R.id.ll_single_item)
        LinearLayout mLinearLayoutCurrency;

        Token mToken;

        TokenHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() >= 0) {
                        mTokenAdapter.getTokenList().get(getAdapterPosition()).setSubscribe(!mToken.isSubscribe());
                        getPresenter().onSubscribeChanged(mToken);
                        mTokenAdapter.notifyItemChanged(getAdapterPosition());
                    }
                }
            });
        }

        void bindToken(Token currency) {
            mTextViewCurrency.setText(currency.getContractName());
            mToken = currency;
            if (currency.isSubscribe()) {
                mImageViewCheckIndicator.setVisibility(View.VISIBLE);
            } else {
                mImageViewCheckIndicator.setVisibility(View.GONE);
            }
        }
    }

    public class TokenAdapter extends RecyclerView.Adapter<TokenHolder> {

        List<Token> mTokenList;
        int resId;

        public TokenAdapter(List<Token> tokenList, int resId) {
            mTokenList = tokenList;
            this.resId = resId;
        }

        @Override
        public TokenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(resId, parent, false);
            return new TokenHolder(view);
        }

        @Override
        public void onBindViewHolder(TokenHolder holder, int position) {
            holder.bindToken(mTokenList.get(position));
        }

        @Override
        public int getItemCount() {
            return mTokenList.size();
        }

        void setFilter(List<Token> newList) {
            mTokenList = new ArrayList<>();
            mTokenList.addAll(newList);
            notifyDataSetChanged();
        }

        List<Token> getTokenList() {
            return mTokenList;
        }
    }
}