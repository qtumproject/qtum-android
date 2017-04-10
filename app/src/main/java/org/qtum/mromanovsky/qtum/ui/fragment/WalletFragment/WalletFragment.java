package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarPagerAdapter.FixedSpeedScroller;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarPagerAdapter.WalletAppBarPagerAdapter;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

    private WalletAppBarPagerAdapter mWalletAppBarPagerAdapter;
    private Animation mAnimation;
    private boolean mIsVisible = false;
    private boolean mIsInitialInitialize = true;
    private LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisibleItems;
    private boolean mLoadingFlag = false;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.bt_qr_code)
    ImageButton mButtonQrCode;
    @BindView(R.id.tv_total_balance)
    TextView mTextViewTotalBalance;
    @BindView(R.id.tv_total_balance_number)
    TextView mTextViewTotalBalanceNumber;
    @BindView(R.id.tv_wallet_name)
    TextView mTextViewWalletName;
    @BindView(R.id.ibtn_left)
    ImageButton mImageButtonLeft;
    @BindView(R.id.ibtn_right)
    ImageButton mImageButtonRight;

    @OnClick({R.id.fab, R.id.bt_qr_code,R.id.ibtn_left,R.id.ibtn_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mWalletFragmentPresenter.sharePubKey();
                break;
            case R.id.bt_qr_code:
                getPresenter().onClickQrCode();
                break;
            case R.id.ibtn_left:
                if(mViewPager.getCurrentItem()>0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                }
                break;
            case R.id.ibtn_right:
                if(mViewPager.getChildCount()>mViewPager.getCurrentItem()) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                }
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
        mTransactionAdapter = null;
    }

    @Override
    public void updateBalance(String balance) {
        String s = balance;
        mTextViewTotalBalanceNumber.setText(balance + " QTUM");
        mWalletAppBarPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem()).updateBalance(balance);
    }

    @Override
    public void updatePubKey(String pubKey) {
        mWalletAppBarPagerAdapter.getRegisteredFragment(mViewPager.getCurrentItem()).updatePubKey(pubKey);
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
    public void initializeViews() {
        ((MainActivity) getActivity()).showBottomNavigationView();

        mWalletAppBarPagerAdapter = new WalletAppBarPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mWalletAppBarPagerAdapter);
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            Interpolator interpolator = new LinearInterpolator();
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), interpolator,true);
            scroller.setDuration(300);
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        final Animation animationWallet =  AnimationUtils.loadAnimation(getContext(), R.anim.alpha_balance_hide);
        animationWallet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTextViewWalletName.setText("New wallet");
                mTextViewWalletName.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alpha_balance_show));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(mIsInitialInitialize){
                    getPresenter().onInitialInitialize();
                    mIsInitialInitialize = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                getPresenter().changePage();
                mIsInitialInitialize = true;
                mTextViewWalletName.startAnimation(animationWallet);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
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

        mTextViewTotalBalance.setVisibility(View.INVISIBLE);
        mTextViewTotalBalanceNumber.setVisibility(View.INVISIBLE);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    if (verticalOffset == 0) {
                        mSwipeRefreshLayout.setEnabled(true);
                    } else {
                        mSwipeRefreshLayout.setEnabled(false);
                    }
                }

                if(verticalOffset==0){
                    if(mIsVisible) {
                        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_balance_hide);
                        mTextViewTotalBalanceNumber.startAnimation(mAnimation);
                        mTextViewTotalBalance.startAnimation(mAnimation);

                        mTextViewTotalBalance.setVisibility(View.INVISIBLE);
                        mTextViewTotalBalanceNumber.setVisibility(View.INVISIBLE);
                        mIsVisible=false;
                    }
                } else if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if(!mIsVisible) {
                        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_balance_show);
                        mTextViewTotalBalanceNumber.startAnimation(mAnimation);
                        mTextViewTotalBalance.startAnimation(mAnimation);

                        mTextViewTotalBalanceNumber.setVisibility(View.VISIBLE);
                        mTextViewTotalBalance.setVisibility(View.VISIBLE);
                        mIsVisible = true;
                    }
                }

            }
        });
    }

    public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

        public void setHistoryList(List<History> historyList) {
            mHistoryList = historyList;
        }
    }

    class ProgressBarHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        public ProgressBarHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bindProgressBar(){
            if(mLoadingFlag){
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }


    public class TransactionHolder extends RecyclerView.ViewHolder {

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
                mLinearLayoutTransaction.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.background_white_with_grey_pressed));
            } else {
                mTextViewDate.setText("Not confirmed");
                mLinearLayoutTransaction.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.background_grey_with_grey_pressed));
            }
            if (history.getChangeInBalance().doubleValue() > 0) {
                mTextViewOperationType.setText(R.string.received);
                //mTextViewID.setText(history.getFromAddress());
                mTextViewID.setText(history.getTxHash());
                mImageViewIcon.setImageResource(R.drawable.ic_received);
                mTextViewOperationType.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
            } else {
                mTextViewOperationType.setText(R.string.sent);
                //mTextViewID.setText(history.getToAddress());
                mTextViewID.setText(history.getTxHash());
                mImageViewIcon.setImageResource(R.drawable.ic_sent);
                mTextViewOperationType.setTextColor(ContextCompat.getColor(getContext(), R.color.pink));
            }
            mTextViewValue.setText(history.getChangeInBalance().toString() + " QTUM");
        }
    }
}