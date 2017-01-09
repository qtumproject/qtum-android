package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.utils.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletFragment extends BaseFragment implements WalletFragmentView {

    public static final int  LAYOUT = R.layout.fragment_wallet;
    public static final String TAG = "WalletFragment";

    WalletFragmentPresenterImpl mWalletFragmentPresenter;
    TransactionAdapter mTransactionAdapter;

    @BindView(R.id.tv_public_key) TextView mTextViewPublicKey;
    @BindView(R.id.tv_balance) TextView mTextViewBalance;
    @BindView(R.id.fab) FloatingActionButton mFloatingActionButton;
    @BindView(R.id.receive) LinearLayout mReceive;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;


    @OnClick({R.id.fab,R.id.receive})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.fab:
//                mWalletFragmentPresenter.onFabClick();
                break;
            case R.id.receive:
                getPresenter().onClickReceive();
        }
    }

    public static WalletFragment newInstance(){
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
    public void updateRecyclerView(List<Transaction> list) {
        if(mTransactionAdapter == null){
            mTransactionAdapter = new TransactionAdapter(list);
            mRecyclerView.setAdapter(mTransactionAdapter);
        } else {
            mTransactionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initializeViews() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
        }
        ((MainActivity) getActivity()).showBottomNavigationView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),"Refresh",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //    @Override
//    public void updatePublicKey(final String publicKey) {
//        getView().post(new Runnable() {
//            @Override
//            public void run() {
//                mTextViewPublicKey.setText(publicKey);
//        }
//        });
//    }
//
//    @Override
//    public void updateBalance(final String balance) {
//        getView().post(new Runnable() {
//            @Override
//            public void run() {
//                mTextViewBalance.setText(balance);
//            }
//        });
//    }
    public class TransactionAdapter  extends RecyclerView.Adapter<TransactionHolder>{

        private List<Transaction> mTransactionList;
        Transaction transaction;

        public TransactionAdapter(List<Transaction> list){
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
            transaction = mTransactionList.get(position);
            holder.bindTransactionData(transaction);
        }

        @Override
        public int getItemCount() {
            return mTransactionList.size();
        }
    }

    public class TransactionHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_value) TextView mTextViewValue;
        @BindView(R.id.tv_date) TextView mTextViewDate;
        @BindView(R.id.tv_id) TextView mTextViewID;
        @BindView(R.id.tv_operation_type) TextView mTextViewOperationType;
        @BindView(R.id.iv_icon)
        ImageView mImageViewIcon;


        public TransactionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().openTransactionFragment(getAdapterPosition());
                }
            });
            ButterKnife.bind(this,itemView);
        }

        public void bindTransactionData(Transaction transaction){
            mTextViewDate.setText(transaction.getDate());
            mTextViewID.setText(transaction.getID());

            if(transaction.getValue()>0){
                mTextViewOperationType.setText(R.string.received);
                mImageViewIcon.setImageResource(R.drawable.received_transaction);
                mTextViewOperationType.setTextColor(mTextViewOperationType.getResources().getColor(R.color.colorAccent));
            }else {
                mTextViewOperationType.setText(R.string.sent);
                mImageViewIcon.setImageResource(R.drawable.sent_transaction);
                mTextViewOperationType.setTextColor(mTextViewOperationType.getResources().getColor(R.color.pink));
            }
            mTextViewValue.setText(transaction.getValue() + " QTUM");
        }

    }
}
