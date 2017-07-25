package com.pixelplex.qtum.ui.fragment.CurrencyFragment;

import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.dataprovider.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.model.Currency;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.model.gson.tokenBalance.TokenBalance;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.SendFragment.SendFragment;
import com.pixelplex.qtum.utils.FontTextView;
import com.pixelplex.qtum.ui.fragment.SendFragment.SendFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrencyFragment extends BaseFragment implements CurrencyFragmentView{

    private CurrencyFragmentPresenterImpl mCurrencyFragmentPresenter;
    private CurrencyAdapter mCurrencyAdapter;
    private String mSearchString;
    private List<Currency> mCurrentList;
    private UpdateService mUpdateService;

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

    public static CurrencyFragment newInstance() {

        Bundle args = new Bundle();

        CurrencyFragment fragment = new CurrencyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mCurrencyFragmentPresenter = new CurrencyFragmentPresenterImpl(this);
    }

    @Override
    protected CurrencyFragmentPresenterImpl getPresenter() {
        return mCurrencyFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_currency;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mUpdateService = getMainActivity().getUpdateService();
        mTextViewCurrencyTitle.setText(R.string.currency);


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
                    mCurrencyAdapter.setFilter(mCurrentList);
                } else {
                    mSearchString = editable.toString().toLowerCase();
                    List<Currency> newList = new ArrayList<>();
                    for(Currency currency: mCurrentList){
                        if(currency.getName().toLowerCase().contains(mSearchString))
                            newList.add(currency);
                    }
                    mCurrencyAdapter.setFilter(newList);
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
    public void setCurrencyList(List<Currency> currencyList) {
        mCurrencyAdapter = new CurrencyAdapter(currencyList);
        mCurrentList = currencyList;
        mRecyclerView.setAdapter(mCurrencyAdapter);
    }

    class CurrencyHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.root_layout)
        RelativeLayout rootLayout;

        @BindView(R.id.token_name)
        FontTextView mTextViewCurrencyName;

        @BindView(R.id.token_balance)
        FontTextView mTextViewCurrencyBalance;

        @BindView(R.id.spinner)
        ProgressBar spinner;

        Currency mCurrency;

        CurrencyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SendFragment) getTargetFragment()).onCurrencyChoose(mCurrency.getName());
                    ((SendFragment) getTargetFragment()).onCurrencyChoose(mCurrency.getAddress());
                    getActivity().onBackPressed();
                }
            });

        }

        void bindCurrency(Currency currency){

            if(this.mCurrency != null && mCurrency.isToken()) {
                mUpdateService.removeTokenBalanceChangeListener(mCurrency.getAddress());
            }

            mCurrency = currency;
            mTextViewCurrencyName.setText(currency.getName());
            if(mCurrency.isToken()) {
                mTextViewCurrencyBalance.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                mUpdateService.addTokenBalanceChangeListener(mCurrency.getAddress(), new TokenBalanceChangeListener() {
                    @Override
                    public void onBalanceChange(final TokenBalance tokenBalance) {
                        rootLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                spinner.setVisibility(View.GONE);
                                mTextViewCurrencyBalance.setText(String.format("%f QTUM", tokenBalance.getTotalBalance()));
                                mTextViewCurrencyBalance.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            } else {
                mTextViewCurrencyBalance.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        }
    }

    private class CurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder> {

        private List<Currency> mCurrencyList;

        public CurrencyAdapter(List<Currency> currencyList) {
            mCurrencyList = currencyList;
        }

        public Currency get(int adapterPosition) {
            return mCurrencyList.get(adapterPosition);
        }

        @Override
        public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CurrencyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_token_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CurrencyHolder holder, int position) {
            holder.bindCurrency(mCurrencyList.get(position));
        }

        void setFilter(List<Currency> currencyListNew){
            mCurrencyList = new ArrayList<>();
            mCurrencyList.addAll(currencyListNew);
            notifyDataSetChanged();
        }

        public void setTokenList(List<Currency> currencyList) {
            this.mCurrencyList = currencyList;
        }

        @Override
        public int getItemCount() {
            return mCurrencyList.size();
        }
    }
}