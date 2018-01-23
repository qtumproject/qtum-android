package org.qtum.wallet.ui.fragment.token_fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveFragment;
import org.qtum.wallet.ui.fragment.token_cash_management_fragment.AddressesListFragmentToken;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.token_fragment.dialogs.ShareDialogFragment;
import org.qtum.wallet.utils.ClipboardUtils;
import org.qtum.wallet.utils.ContractManagementHelper;
import org.qtum.wallet.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.Subscriber;

public abstract class TokenFragment extends BaseFragment implements TokenView, TokenHistoryClickListener {

    private static final String tokenKey = "tokenInfo";
    private static final String qtumAddressKey = "qtumAddressKey";

    public static final String totalSupply = "totalSupply";
    public static final String decimals = "decimals";
    public static final String symbol = "symbol";
    public static final String name = "name";

    public static BaseFragment newInstance(Context context, Contract token) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, TokenFragment.class);
        args.putSerializable(tokenKey, token);
        fragment.setArguments(args);
        return fragment;
    }

    private TokenPresenter presenter;

    @OnClick(R.id.bt_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @BindView(R.id.ll_balance)
    protected LinearLayout mLinearLayoutBalance;

    @BindView(R.id.tv_balance)
    protected FontTextView mTextViewBalance;

    @BindView(R.id.tv_currency)
    protected FontTextView mTextViewCurrency;

    @BindView(R.id.available_balance_title)
    protected FontTextView balanceTitle;

    @BindView(R.id.tv_unconfirmed_balance)
    protected FontTextView uncomfirmedBalanceValue;

    @BindView(R.id.unconfirmed_balance_title)
    protected FontTextView uncomfirmedBalanceTitle;

    @BindView(R.id.balance_view)
    protected FrameLayout balanceView;

    @BindView(R.id.fade_divider_root)
    RelativeLayout fadeDividerRoot;

    @BindView(R.id.app_bar)
    protected
    AppBarLayout mAppBarLayout;

    @BindView(R.id.tv_token_address)
    FontTextView tokenAddress;

    @BindView(R.id.tv_token_name)
    protected FontTextView mTextViewTokenName;

    @BindView(R.id.contract_address_value)
    FontTextView contractAddress;

    @BindView(R.id.initial_supply_value)
    protected
    FontTextView totalSupplyValue;

    @BindView(R.id.decimal_units_value)
    protected
    FontTextView decimalsValue;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.token_histories_placeholder)
    TextView mTextViewHistoriesPlaceholder;

    @BindView(R.id.recycler_token_history)
    protected RecyclerView mRecyclerView;
    protected TokenHistoryAdapter mAdapter;
    protected LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());

    ShareDialogFragment shareDialog;

    protected int visibleItemCount;
    protected int totalItemCount;
    protected int pastVisibleItems;
    protected boolean mLoadingFlag = false;

    @OnLongClick(R.id.tv_token_address)
    public boolean onAddressLongClick() {
        ClipboardUtils.copyToClipBoard(getContext(), tokenAddress.getText().toString(), new ClipboardUtils.CopyCallback() {
            @Override
            public void onCopyToClipBoard() {
                showToast(getString(R.string.copied));
            }
        });
        return true;
    }

    @OnLongClick(R.id.contract_address_value)
    public boolean onAContractLongClick() {
        ClipboardUtils.copyToClipBoard(getContext(), contractAddress.getText().toString(), new ClipboardUtils.CopyCallback() {
            @Override
            public void onCopyToClipBoard() {
                showToast(getString(R.string.copied));
            }
        });
        return true;
    }

    @OnClick(R.id.bt_share)
    public void onShareClick() {
        shareDialog = ShareDialogFragment.newInstance(presenter.getToken().getContractAddress(), presenter.getAbi());
        shareDialog.show(getFragmentManager(), shareDialog.getClass().getCanonicalName());
    }

    @OnClick(R.id.token_addr_btn)
    public void onTokenAddrClick() {
        BaseFragment receiveFragment = ReceiveFragment.newInstance(getContext(), presenter.getToken().getContractAddress(), mTextViewBalance.getText().toString(), mTextViewCurrency.getText().toString());
        openFragment(receiveFragment);
    }

    @OnClick(R.id.iv_choose_address)
    public void onChooseAddressClick() {
        if (!TextUtils.isEmpty(getCurrency())) {
            BaseFragment addressListFragment = AddressesListFragmentToken.newInstance(getContext(), getPresenter().getToken(), getCurrency());
            openFragment(addressListFragment);
        }
    }

    @Override
    public String getCurrency() {
        return mTextViewCurrency.getText().toString().trim();
    }

    @Override
    protected void createPresenter() {
        presenter = new TokenPresenterImpl(this, new TokenInteractorImpl(getContext()));
    }

    @Override
    protected TokenPresenter getPresenter() {
        return presenter;
    }

    protected float headerPAdding = 0;
    protected float percents = 1;
    protected float prevPercents = 1;

    @Override
    public void initializeViews() {
        super.initializeViews();

        uncomfirmedBalanceValue.setVisibility(View.GONE);
        uncomfirmedBalanceTitle.setVisibility(View.GONE);

        Token token = (Token) getArguments().getSerializable(tokenKey);
        presenter.setToken(token);
        if (token.getLastBalance() != null) {
            setBalance(token.getLastBalance().toPlainString());
        }
        setTokenAddress(token.getContractAddress());
        setSenderAddress(token.getSenderAddress());
        headerPAdding = convertDpToPixel(16, getContext());

        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!mLoadingFlag) {
                        visibleItemCount = mLinearLayoutManager.getChildCount();
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 1) {
                            getPresenter().onLastItem(totalItemCount - 1);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void addHistory(int positionStart, int itemCount, List<TokenHistory> historyList) {
        mAdapter.setHistoryList(historyList);
        mAdapter.setLoadingFlag(false);
        mLoadingFlag = false;
        mAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    protected boolean expanded = false;

    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    protected int getTotalRange() {
        return mAppBarLayout.getTotalScrollRange();
    }

    protected void animateText(float percents, View view, float fringe) {
        if (percents > fringe) {
            view.setScaleX(percents);
            view.setScaleY(percents);
        } else {
            view.setScaleX(fringe);
            view.setScaleY(fringe);
        }
    }

    @Override
    public void setTokenAddress(String address) {
        if (!TextUtils.isEmpty(address)) {
            contractAddress.setText(address);
        }
    }

    @Override
    public void setQtumAddress(String address) {
        if (!TextUtils.isEmpty(address)) {
            tokenAddress.setText(address);
        }
    }

    @Override
    public void updateHistory(List<TokenHistory> tokenHistories) {
        if(tokenHistories.isEmpty()){
            mTextViewHistoriesPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mTextViewHistoriesPlaceholder.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSenderAddress(String address) {
    }

    @Override
    public boolean isAbiEmpty(String abi) {
        return TextUtils.isEmpty(abi);
    }

    @Override
    public Subscriber<String> getTotalSupplyValueCallback() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                onContractPropertyUpdated(TokenFragment.totalSupply, presenter.onTotalSupplyPropertySuccess(getPresenter().getToken(), s));
            }
        };
    }

    @Override
    public Subscriber<String> getDecimalsValueCallback() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                onContractPropertyUpdated(TokenFragment.decimals, s);
                if (s != null) {
                    getPresenter().onDecimalsPropertySuccess(s);
                }
            }
        };
    }

    @Override
    public Subscriber<String> getSymbolValueCallback() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                onContractPropertyUpdated(TokenFragment.symbol, s);
                mAdapter.setSymbol(" " + s);
                mAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public Subscriber<String> getNameValueCallback() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                onContractPropertyUpdated(TokenFragment.name, s);
            }
        };
    }

    @Override
    public void onTokenHistoryClick(int adapterPosition) {

    }
}