package org.qtum.wallet.ui.fragment.currency_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TokenBalanceChangeListener;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.CurrencyToken;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.fragment.token_fragment.TokenFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.utils.ContractManagementHelper;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.SearchBar;
import org.qtum.wallet.utils.SearchBarListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class CurrencyFragment extends BaseFragment implements CurrencyView, SearchBarListener {

    private CurrencyPresenter mCurrencyFragmentPresenter;
    protected CurrencyAdapter mCurrencyAdapter;
    private String mSearchString;
    protected List<Currency> mCurrentList;
    private UpdateService mUpdateService;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    @BindView(R.id.search_bar)
    SearchBar searchBar;


    @BindView(R.id.tv_currency_title)
    TextView mTextViewCurrencyTitle;
    @BindView(R.id.ll_currency)
    RelativeLayout mFrameLayoutBase;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, CurrencyFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mCurrencyFragmentPresenter = new CurrencyPresenterImpl(this, new CurrencyInteractorImpl(getContext()));
    }

    @Override
    protected CurrencyPresenter getPresenter() {
        return mCurrencyFragmentPresenter;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mUpdateService = getMainActivity().getUpdateService();
        mTextViewCurrencyTitle.setText(R.string.currency);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mFrameLayoutBase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    hideKeyBoard();
            }
        });
        searchBar.setListener(this);
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
        if (filter.isEmpty()) {
            mCurrencyAdapter.setFilter(mCurrentList);
        } else {
            mSearchString = filter.toLowerCase();
            List<Currency> newList = new ArrayList<>();
            for (Currency currency : mCurrentList) {
                if (currency.getName().toLowerCase().contains(mSearchString))
                    newList.add(currency);
            }
            mCurrencyAdapter.setFilter(newList);
        }
    }

    class CurrencyHolder extends RecyclerView.ViewHolder implements TokenBalanceChangeListener {

        @BindView(R.id.root_layout)
        RelativeLayout rootLayout;

        @BindView(R.id.token_name)
        FontTextView mTextViewCurrencyName;

        @BindView(R.id.token_balance)
        FontTextView mTextViewCurrencyBalance;

        @BindView(R.id.spinner)
        ProgressBar spinner;

        @BindView(R.id.token_symbol)
        FontTextView mTextViewSymbol;

        Currency mCurrency;

        Token token;

        CurrencyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SendFragment) getTargetFragment()).onCurrencyChoose(mCurrency);
                    getActivity().onBackPressed();
                }
            });
        }

        void bindCurrency(Currency currency) {

            if (this.mCurrency != null && mCurrency instanceof CurrencyToken) {
                mUpdateService.removeTokenBalanceChangeListener(((CurrencyToken) mCurrency).getToken().getContractAddress(), this);
            }

            mCurrency = currency;
            mTextViewCurrencyName.setText(currency.getName());
            if (mCurrency instanceof CurrencyToken) {
                token = ((CurrencyToken) mCurrency).getToken();
                ContractManagementHelper.getPropertyValue(TokenFragment.symbol, ((CurrencyToken) mCurrency).getToken(), getContext())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(String string) {
                                mTextViewSymbol.setVisibility(View.VISIBLE);
                                mTextViewSymbol.setText(string);
                            }
                        });

                mTextViewCurrencyBalance.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);

                mUpdateService.addTokenBalanceChangeListener(token.getContractAddress(), this);

            } else {
                mTextViewCurrencyBalance.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBalanceChange(final TokenBalance tokenBalance) {
            if (token.getContractAddress().equals(tokenBalance.getContractAddress())) {
                token.setLastBalance(tokenBalance.getTotalBalance());

                if (token.getDecimalUnits() == null) {
                    ContractManagementHelper.getPropertyValue(TokenFragment.decimals, token, itemView.getContext())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onNext(String string) {
                                    token = new TinyDB(itemView.getContext()).setTokenDecimals(token, Integer.valueOf(string));
                                    updateBalance();
                                }
                            });

                } else {
                    updateBalance();
                }
            }
        }

        private void updateBalance() {
            rootLayout.post(new Runnable() {
                @Override
                public void run() {
                    spinner.setVisibility(View.GONE);
                    mTextViewCurrencyBalance.setLongNumberText(String.valueOf(((CurrencyToken) mCurrency).getToken().getTokenBalanceWithDecimalUnits()));
                    mTextViewCurrencyBalance.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    protected class CurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder> {

        private int resId;
        private List<Currency> mCurrencyList;

        public CurrencyAdapter(List<Currency> currencyList, int resId) {
            mCurrencyList = currencyList;
            this.resId = resId;
        }

        public Currency get(int adapterPosition) {
            return mCurrencyList.get(adapterPosition);
        }

        @Override
        public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CurrencyHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        }

        @Override
        public void onBindViewHolder(CurrencyHolder holder, int position) {
            holder.bindCurrency(mCurrencyList.get(position));
        }

        void setFilter(List<Currency> currencyListNew) {
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