package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SubscribeTokensFragment extends BaseFragment implements SubscribeTokensFragmentView {

    private SubscribeTokensFragmentPresenter mSubscribeTokensFragmentPresenter;
    private TokenAdapter mTokenAdapter;
    private String mSearchString;
    private List<Token> mCurrentList;


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_search_currency)
    EditText mEditTextSearchCurrency;
    @BindView(R.id.tv_currency_title)
    TextView mTextViewCurrencyTitle;
    @BindView(R.id.ll_currency)
    FrameLayout mFrameLayoutBase;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static SubscribeTokensFragment newInstance() {

        Bundle args = new Bundle();

        SubscribeTokensFragment fragment = new SubscribeTokensFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mSubscribeTokensFragmentPresenter = new SubscribeTokensFragmentPresenter(this);
    }

    @Override
    protected SubscribeTokensFragmentPresenter getPresenter() {
        return mSubscribeTokensFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_currency;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTextViewCurrencyTitle.setText(R.string.chose_to_subscribe);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mEditTextSearchCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().isEmpty()){
                    mTokenAdapter.setFilter(mCurrentList);
                } else {
                    mSearchString = editable.toString().toLowerCase();
                    List<Token> newList = new ArrayList<>();
                    for(Token currency: mCurrentList){
                        if(currency.getContractName().toLowerCase().contains(mSearchString))
                            newList.add(currency);
                    }
                    mTokenAdapter.setFilter(newList);
                }
            }
        });

        mEditTextSearchCurrency.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    mFrameLayoutBase.requestFocus();
                    hideKeyBoard();
                    return false;
                }
                return false;
            }
        });

        mFrameLayoutBase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    hideKeyBoard();
            }
        });
    }

    @Override
    public void setTokenList(List<Token> tokenList) {

        Collections.sort(tokenList, new Comparator<Token>() {
            @Override
            public int compare(Token token, Token t1) {
                if(token.isSubscribe() && !t1.isSubscribe()){
                    return -1;
                } else if(!token.isSubscribe() && t1.isSubscribe()){
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        mTokenAdapter = new TokenAdapter(tokenList);
        mCurrentList = tokenList;
        mRecyclerView.setAdapter(mTokenAdapter);
    }

    @Override
    public List<Token> getTokenList() {
        return mTokenAdapter.getTokenList();
    }

    public class TokenHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_single_string)
        TextView mTextViewCurrency;
        @BindView(R.id.iv_check_indicator_blue)
        ImageView mImageViewCheckIndicator;
        @BindView(R.id.ll_single_item)
        LinearLayout mLinearLayoutCurrency;

        Token mToken;

        TokenHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(getAdapterPosition()>=0) {
                            mTokenAdapter.getTokenList().get(getAdapterPosition()).setSubscribe(!mToken.isSubscribe());
                            //TODO notifyItemSetChanged
                            mTokenAdapter.notifyItemChanged(getAdapterPosition());
                           // mTokenAdapter.notifyDataSetChanged();
                        }
                    }
                });
        }

        void bindToken(Token currency){
            mTextViewCurrency.setText(currency.getContractName());
            mToken = currency;
            if(currency.isSubscribe()){
                mImageViewCheckIndicator.setVisibility(View.VISIBLE);
            } else {
                mImageViewCheckIndicator.setVisibility(View.GONE);
            }
        }
    }

    public class TokenAdapter extends RecyclerView.Adapter<TokenHolder>{

        List<Token> mTokenList;

        TokenAdapter(List<Token> tokenList){
            mTokenList = tokenList;
        }

        @Override
        public TokenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_single_checkable, parent, false);
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

        void setFilter(List<Token> newList){
            mTokenList = new ArrayList<>();
            mTokenList.addAll(newList);
            notifyDataSetChanged();
        }

        List<Token> getTokenList() {
            return mTokenList;
        }
    }
}