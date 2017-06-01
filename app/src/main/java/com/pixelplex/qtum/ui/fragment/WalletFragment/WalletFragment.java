package com.pixelplex.qtum.ui.fragment.WalletFragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.WalletFragment.WalletAppBarPagerAdapter.FixedSpeedScroller;
import com.pixelplex.qtum.ui.fragment.WalletFragment.WalletAppBarPagerAdapter.WalletAppBarPagerAdapter;
import com.pixelplex.qtum.utils.FontTextView;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletFragment extends BaseFragment implements WalletFragmentView {

    public final int LAYOUT = R.layout.fragment_wallet;

    private WalletFragmentPresenterImpl mWalletFragmentPresenter;
    private TransactionAdapter mTransactionAdapter;

    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisibleItems;
    private boolean mLoadingFlag = false;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.bt_qr_code)
    ImageButton mButtonQrCode;
    @BindView(R.id.tv_wallet_name)
    TextView mTextViewWalletName;

    @BindView(R.id.fade_divider)
    View fadeDivider;

    @BindView(R.id.tv_public_key)
    FontTextView publicKeyValue;

    @OnClick(R.id.ll_receive)
    public void onReceiveClick(){
        mWalletFragmentPresenter.onReceiveClick();
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

    @OnClick({R.id.bt_qr_code,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_qr_code:
                getPresenter().onClickQrCode();
                break;
        }
    }

    public static WalletFragment newInstance() {

        Bundle args = new Bundle();

        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mWalletFragmentPresenter = new WalletFragmentPresenterImpl(this);
    }

    @Override
    protected WalletFragmentPresenterImpl getPresenter() {
        return mWalletFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void updateHistory(List<History> historyList) {
        mTransactionAdapter = new TransactionAdapter(historyList);
        mRecyclerView.setAdapter(mTransactionAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadingFlag = false;
    }

    @Override
    public void setAdapterNull() {
    }

    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
       balanceValue.setText(String.format("%s QTUM",balance));
        if(unconfirmedBalance != null) {
            uncomfirmedBalanceValue.setVisibility(View.VISIBLE);
            uncomfirmedBalanceTitle.setVisibility(View.VISIBLE);
            uncomfirmedBalanceValue.setText(String.format("%s QTUM", unconfirmedBalance));
        } else {
            uncomfirmedBalanceValue.setVisibility(View.GONE);
            uncomfirmedBalanceTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void updatePubKey(String pubKey) {
      publicKeyValue.setText(pubKey);
    }

    @Override
    public void startRefreshAnimation() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefreshRecyclerAnimation() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void addHistory(int positionStart, int itemCount, List<History> historyList) {
        mTransactionAdapter.setHistoryList(historyList);
        mLoadingFlag = false;
        mTransactionAdapter.notifyItemRangeChanged(positionStart,itemCount);
    }

    @Override
    public void loadNewHistory() {
        mLoadingFlag = true;
        mTransactionAdapter.notifyItemChanged(totalItemCount-1);
    }

    @Override
    public void notifyNewHistory() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void notifyConfirmHistory(final int notifyPosition) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionAdapter.notifyItemChanged(notifyPosition);
            }
        });
    }

    @Override
    public void setWalletName(String walletName) {
        //TODO WALLET NAME
    }

    @Override
    public void notifyNewToken() {
       //TODO NOTIFY TOKEN
    }

    final DisplayMetrics dm = new DisplayMetrics();


    @Override
    public void initializeViews() {

        uncomfirmedBalanceValue.setVisibility(View.GONE);
        uncomfirmedBalanceTitle.setVisibility(View.GONE);

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        ((MainActivity) getActivity()).showBottomNavigationView();

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    if(!mLoadingFlag) {
                        visibleItemCount = mLinearLayoutManager.getChildCount();
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount-1) {
                            getPresenter().onLastItem(totalItemCount-1);
                        }
                    }

                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         getPresenter().onRefresh();
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

                        float percents = (((getTotalRange() - Math.abs(verticalOffset))*1.0f)/getTotalRange());

                        balanceView.setAlpha((percents>0.5f)? percents : 1 - percents);

                        fadeDivider.setVisibility((percents == 0)? View.VISIBLE : View.INVISIBLE);

                        if(uncomfirmedBalanceTitle.getVisibility() == View.VISIBLE) {
                            animateText(percents, balanceTitle);
                            balanceTitle.setX(appBarLayout.getWidth() / 2 * percents - (balanceTitle.getWidth() * percents) / 2);

                            animateText(percents, balanceValue);
                            balanceValue.setX(appBarLayout.getWidth() - (appBarLayout.getWidth() / 2 * percents + (balanceValue.getWidth() * percents) / 2) - balanceValue.getWidth() * (1 - percents));
                            balanceValue.setY(balanceView.getHeight() / 2 - balanceTitle.getHeight() * percents - balanceValue.getHeight() * percents - balanceValue.getHeight() * (1 - percents));


                            animateText(percents, uncomfirmedBalanceValue);
                            uncomfirmedBalanceValue.setX(appBarLayout.getWidth() - (appBarLayout.getWidth() / 2 * percents + (uncomfirmedBalanceValue.getWidth() * percents) / 2) - uncomfirmedBalanceValue.getWidth() * (1 - percents));

                            animateText(percents, uncomfirmedBalanceTitle);
                            uncomfirmedBalanceTitle.setY(balanceView.getHeight() / 2 + uncomfirmedBalanceValue.getHeight() * percents - (uncomfirmedBalanceTitle.getHeight() * percents * (1 - percents)));
                            uncomfirmedBalanceTitle.setX(appBarLayout.getWidth() / 2 * percents - (uncomfirmedBalanceTitle.getWidth() * percents) / 2);
                        } else {

                            animateText(percents, balanceTitle);
                            balanceTitle.setX(appBarLayout.getWidth() / 2 * percents - (balanceTitle.getWidth() * percents) / 2);
                            balanceTitle.setY(balanceView.getHeight()/2 + balanceTitle.getHeight()/2 * percents - balanceTitle.getHeight()/2 * (1-percents));

                            animateText(percents, balanceValue);
                            balanceValue.setX(appBarLayout.getWidth() - (appBarLayout.getWidth() / 2 * percents + (balanceValue.getWidth() * percents) / 2) - balanceValue.getWidth() * (1 - percents));
                            balanceValue.setY(balanceView.getHeight() / 2 - balanceValue.getHeight() * percents - balanceValue.getHeight()/2 * (1-percents));

                        }

                    }
                });
        mWalletFragmentPresenter.notifyHeader();
    }

    public int getTotalRange() {
        return mAppBarLayout.getTotalScrollRange();
    }

    private void animateText(float percents, View view) {
        if(percents>0.9f) {
            view.setScaleX(percents);
            view.setScaleY(percents);
        }
    }

    private class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<History> mHistoryList;
        History mHistory;

        private final int TYPE_PROGRESS_BAR = 0;
        private final int TYPE_TRANSACTION = 1;

        TransactionAdapter(List<History> historyList) {
            mHistoryList = historyList;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == mHistoryList.size()){
                return TYPE_PROGRESS_BAR;
            }
            return TYPE_TRANSACTION;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_TRANSACTION) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.item_transaction, parent, false);
                return new TransactionHolder(view);
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.item_progress_bar, parent, false);
                return new ProgressBarHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof ProgressBarHolder){
                ((ProgressBarHolder)holder).bindProgressBar();
            } else {
                mHistory = mHistoryList.get(position);
                ((TransactionHolder) holder).bindTransactionData(mHistory);
            }
        }

        @Override
        public int getItemCount() {
            return mHistoryList.size()+1;
        }

        void setHistoryList(List<History> historyList) {
            mHistoryList = historyList;
        }
    }

    class ProgressBarHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        ProgressBarHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void bindProgressBar(){
            if(mLoadingFlag){
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }


    class TransactionHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_value)
        TextView mTextViewValue;
        @BindView(R.id.tv_date)
        TextView mTextViewDate;
        @BindView(R.id.tv_id)
        TextView mTextViewID;
        @BindView(R.id.tv_operation_type)
        TextView mTextViewOperationType;
        @BindView(R.id.iv_icon)
        ImageView mImageViewIcon;
        @BindView(R.id.ll_transaction)
        LinearLayout mLinearLayoutTransaction;

        Date date = new Date();
        long currentTime = date.getTime() / 1000L;

        TransactionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().openTransactionFragment(getAdapterPosition());
                }
            });
            ButterKnife.bind(this, itemView);
        }

        void bindTransactionData(History history) {

            if(history.getBlockTime() != null) {
                long transactionTime = history.getBlockTime();
                long delay = currentTime - transactionTime;
                String dateString;
                if (delay < 3600) {
                    dateString = delay / 60 + " min ago";
                } else {

                    Calendar calendarNow = Calendar.getInstance();
                    calendarNow.set(Calendar.HOUR_OF_DAY, 0);
                    calendarNow.set(Calendar.MINUTE, 0);
                    calendarNow.set(Calendar.SECOND, 0);
                    date = calendarNow.getTime();

                    Date dateTransaction = new Date(transactionTime * 1000L);
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(dateTransaction);
                    if ((transactionTime - date.getTime() / 1000L) > 0) {
                        dateString = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                    } else {
                        dateString = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ", " + calendar.get(Calendar.DATE);
                    }
                }
                mTextViewDate.setText(dateString);
            } else {
                mTextViewDate.setText("Not confirmed");
            }
            if (history.getChangeInBalance().doubleValue() > 0) {
                mTextViewOperationType.setText(R.string.received);
                mTextViewID.setText(history.getTxHash());
                mImageViewIcon.setImageResource(R.drawable.ic_received);
            } else {
                mTextViewOperationType.setText(R.string.sent);
                mTextViewID.setText(history.getTxHash());
                mImageViewIcon.setImageResource(R.drawable.ic_sent);
            }
            mTextViewValue.setText(history.getChangeInBalance().toString() + " QTUM");
        }
    }
}