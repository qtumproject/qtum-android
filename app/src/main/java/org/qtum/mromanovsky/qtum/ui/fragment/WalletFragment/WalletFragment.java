package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletFragment extends BaseFragment implements WalletFragmentView {

    public static final int LAYOUT = R.layout.fragment_wallet;
    public static final String TAG = "WalletFragment";

    WalletFragmentPresenterImpl mWalletFragmentPresenter;
    TransactionAdapter mTransactionAdapter;

    @BindView(R.id.tv_public_key)
    TextView mTvPublicKey;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.ll_receive)
    LinearLayout mLinearLayoutReceive;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;


    @OnClick({R.id.fab, R.id.ll_receive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mWalletFragmentPresenter.sharePubKey();
                break;
            case R.id.ll_receive:
                getPresenter().onClickReceive();
        }
    }

    public static WalletFragment newInstance() {
        WalletFragment walletFragment = new WalletFragment();
        return walletFragment;
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
    public void updateRecyclerView(List<TransactionQTUM> list) {
        mTransactionAdapter = new TransactionAdapter(list);
        mRecyclerView.setAdapter(mTransactionAdapter);
    }

    @Override
    public void setAdapterNull() {
        mTransactionAdapter = null;
    }

    @Override
    public void updateBalance(double balance) {
        mTvBalance.setText(String.valueOf(balance));
    }

    @Override
    public void updateData(double balance) {
        mTvBalance.setText(String.valueOf(balance));
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updatePubKey(String pubKey) {
        mTvPublicKey.setText(pubKey);
    }

    @Override
    public void startRefreshAnimation() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void initializeViews() {

        ((MainActivity) getActivity()).showBottomNavigationView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().onRefresh();
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    public class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder> {

        private List<TransactionQTUM> mTransactionList;
        TransactionQTUM mTransaction;

        public TransactionAdapter(List<TransactionQTUM> list) {
            mTransactionList = list;
        }

        @Override
        public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_transaction, parent, false);
            return new TransactionHolder(view);
        }

        @Override
        public void onBindViewHolder(TransactionHolder holder, int position) {
            mTransaction = mTransactionList.get(position);
            holder.bindTransactionData(mTransaction);
        }

        @Override
        public int getItemCount() {
            return mTransactionList.size();
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

        Date date = new Date();
        long currentTime = date.getTime()/1000L;

        public TransactionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().openTransactionFragment(getAdapterPosition());
                }
            });
            ButterKnife.bind(this, itemView);
        }

        public void bindTransactionData(TransactionQTUM transaction) {
            mTextViewID.setText(transaction.getAddress());

            long transactionTime = transaction.getTime();
            long delay = currentTime - transactionTime;
            String dateString;
            if(delay<3600){
                dateString = delay/60 + " min ago";
            } else {
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(0);
                Date dateTransaction = new Date(transactionTime*1000L);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(dateTransaction);
                if((transactionTime - date.getTime()/1000L)>0){
                    dateString = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
                } else {
                    dateString = calendar.getDisplayName(Calendar.MONTH,Calendar.SHORT, Locale.US) + ", " + calendar.get(Calendar.DATE);
                }
            }
            mTextViewDate.setText(dateString);

            if (transaction.getAmount() > 0) {
                mTextViewOperationType.setText(R.string.received);
                mImageViewIcon.setImageResource(R.drawable.ic_received_transaction);
                mTextViewOperationType.setTextColor(mTextViewOperationType.getResources().getColor(R.color.colorAccent));
            } else {
                mTextViewOperationType.setText(R.string.sent);
                mImageViewIcon.setImageResource(R.drawable.ic_sent_transaction);
                mTextViewOperationType.setTextColor(mTextViewOperationType.getResources().getColor(R.color.pink));
            }
            mTextViewValue.setText(transaction.getAmount() + " QTUM");
        }
    }
}
