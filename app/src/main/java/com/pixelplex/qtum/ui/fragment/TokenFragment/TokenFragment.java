package com.pixelplex.qtum.ui.fragment.TokenFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FontTextView;
import com.pixelplex.qtum.utils.ResizeWidthAnimation;
import com.pixelplex.qtum.utils.StackCollapseLinearLayout;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by kirillvolkov on 01.06.17.
 */

public class TokenFragment extends BaseFragment implements TokenFragmentView {

    public final int LAYOUT = R.layout.lyt_token_fragment;
    public static final String tokenKey = "tokenInfo";

    public static final String totalSupply = "totalSupply";
    public static final String decimals = "decimals";

    public static TokenFragment newInstance(Contract token) {
        Bundle args = new Bundle();
        TokenFragment fragment = new TokenFragment();
        args.putSerializable(tokenKey,token);
        fragment.setArguments(args);
        return fragment;
    }

    TokenFragmentPresenter presenter;

    @OnClick(R.id.bt_back)
    public void onBackClick(){
        getActivity().onBackPressed();
    }

    //HEADER
    @BindView(R.id.tv_balance)
    FontTextView balanceValue;
    @BindView(R.id.available_balance_title)
    FontTextView balanceTitle;

    @BindView(R.id.tv_unconfirmed_balance)
    FontTextView uncomfirmedBalanceValue;
    @BindView(R.id.unconfirmed_balance_title)
    FontTextView uncomfirmedBalanceTitle;
    //HEADER

    @BindView(R.id.balance_view)
    FrameLayout balanceView;

    @BindView(R.id.fade_divider)
    View fadeDivider;

    @BindView(R.id.fade_divider_root)
    RelativeLayout fadeDividerRoot;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.collapse_layout)
    StackCollapseLinearLayout collapseLinearLayout;

    @BindView(R.id.tv_token_address)
    FontTextView tokenAddress;

    @BindView(R.id.contract_address_value)
    FontTextView contractAddress;

    @BindView(R.id.initial_supply_value)
    FontTextView totalSupplyValue;

    @BindView(R.id.decimal_units_value)
    FontTextView decimalsValue;

    @BindView(R.id.sender_address_value)
    FontTextView senderAddrValue;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void createPresenter() {
        presenter = new TokenFragmentPresenter(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    float headerPAdding = 0;
    float percents = 1;
    float prevPercents = 1;

    @Override
    public void initializeViews() {
        super.initializeViews();

        // Disable "Drag" for AppBarLayout (i.e. User can't scroll appBarLayout by directly touching appBarLayout - User can only scroll appBarLayout by only using scrollContent)
        if (mAppBarLayout.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }

        presenter.setToken((Token) getArguments().getSerializable(tokenKey));
        presenter.getPropertyValue(totalSupply);
        presenter.getPropertyValue(decimals);

        collapseLinearLayout.requestLayout();
        headerPAdding = convertDpToPixel(16,getContext());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == SCROLL_STATE_IDLE){
                    autodetectAppbar();
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        uncomfirmedBalanceValue.setVisibility(View.GONE);
        uncomfirmedBalanceTitle.setVisibility(View.GONE);

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefresh();
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (!mSwipeRefreshLayout.isActivated()) {
                    if (verticalOffset == 0) {
                        mSwipeRefreshLayout.setEnabled(true);
                    } else {
                        mSwipeRefreshLayout.setEnabled(false);
                    }
                }

                percents = (((getTotalRange() - Math.abs(verticalOffset))*1.0f)/getTotalRange());

                balanceView.setAlpha((percents>0.5f)? percents : 1 - percents);

                if(percents == 0){
                    doDividerExpand();
                } else {
                    doDividerCollapse();
                }

                final float textPercent = (percents >= .5f)? percents : .5f;
                final float textPercent3f = (percents >= .3f)? percents : .3f;

                if(uncomfirmedBalanceTitle.getVisibility() == View.VISIBLE) {
                    animateText(percents, balanceValue, .5f);
                    balanceValue.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (balanceValue.getWidth() * textPercent) / 2) - balanceValue.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));
                    balanceValue.setY(balanceView.getHeight() / 2 - balanceTitle.getHeight() * percents - balanceValue.getHeight() * percents - balanceValue.getHeight() * (1 - percents));

                    animateText(percents, balanceTitle, .7f);
                    balanceTitle.setX(balanceView.getWidth() / 2 * percents - (balanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                    balanceTitle.setY(balanceView.getHeight() / 2 - balanceTitle.getHeight() * percents - balanceTitle.getHeight() * (1 - percents) );

                    animateText(percents, uncomfirmedBalanceValue, .5f);
                    uncomfirmedBalanceValue.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (uncomfirmedBalanceValue.getWidth() * textPercent) / 2) - uncomfirmedBalanceValue.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));

                    animateText(percents, uncomfirmedBalanceTitle, .7f);
                    uncomfirmedBalanceTitle.setY(balanceView.getHeight() / 2 + uncomfirmedBalanceValue.getHeight() * percents - (uncomfirmedBalanceTitle.getHeight() * percents * (1 - percents)));
                    uncomfirmedBalanceTitle.setX(balanceView.getWidth() / 2 * percents - (uncomfirmedBalanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                } else {
                    animateText(percents, balanceTitle, .7f);
                    balanceTitle.setX(balanceView.getWidth() / 2 * percents - (balanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                    balanceTitle.setY(balanceView.getHeight() / 2 + balanceTitle.getHeight() / 2 * percents - balanceTitle.getHeight() / 2 * (1-percents));

                    animateText(percents, balanceValue, .5f);
                    balanceValue.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (balanceValue.getWidth() * textPercent) / 2) - balanceValue.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));
                    balanceValue.setY(balanceView.getHeight() / 2 - balanceValue.getHeight() * percents - balanceValue.getHeight() / 2 * (1-percents));
                }
                collapseLinearLayout.collapseFromPercents(percents);
                prevPercents = percents;
            }

        });
        doDividerCollapse();
    }

    boolean expanded = false;

    public void doDividerExpand() {
        if(!expanded) {
            expanded = true;
            fadeDivider.clearAnimation();
            ResizeWidthAnimation anim = new ResizeWidthAnimation(fadeDivider, getResources().getDisplayMetrics().widthPixels);
            anim.setDuration(300);
            anim.setFillEnabled(true);
            anim.setFillAfter(true);
            fadeDivider.startAnimation(anim);
        }
    }

    public void doDividerCollapse() {
        if(expanded) {
            fadeDivider.clearAnimation();
            fadeDivider.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams lp = fadeDivider.getLayoutParams();
            lp.width = 0;
            fadeDivider.setLayoutParams(lp);
            expanded = false;
        }
    }

    private void autodetectAppbar(){
        if(percents >=.5f){
            mAppBarLayout.setExpanded(true, true);
        } else {
            mAppBarLayout.setExpanded(false, true);
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public int getTotalRange() {
        return mAppBarLayout.getTotalScrollRange();
    }

    private void animateText(float percents, View view, float fringe) {
        if(percents > fringe) {
            view.setScaleX(percents);
            view.setScaleY(percents);
        } else {
            view.setScaleX(fringe);
            view.setScaleY(fringe);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setBalance(float balance) {
        balanceValue.setText(String.format("%f QTUM",balance));
    }

    @Override
    public void setTokenAddress(String address) {
        if(!TextUtils.isEmpty(address)) {
            tokenAddress.setText(address);
            contractAddress.setText(address);
        }
    }

    @Override
    public void onContractPropertyUpdated(String propName, String propValue) {
        switch (propName){
            case totalSupply:
                totalSupplyValue.setText(propValue);
                break;
            case decimals:
                decimalsValue.setText(propValue);
                break;
        }
    }

    @Override
    public void setSenderAddress(String address) {
        if(!TextUtils.isEmpty(address)) {
            senderAddrValue.setText(address);
        }
    }
}
