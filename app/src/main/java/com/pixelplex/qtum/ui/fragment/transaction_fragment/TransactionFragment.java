package com.pixelplex.qtum.ui.fragment.transaction_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment.TransactionDetailFragment;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class TransactionFragment extends BaseFragment implements TransactionFragmentView {

    @BindView(R.id.tv_value)
    TextView mTextViewValue;
    @BindView(R.id.tv_received_time)
    TextView mTextViewReceivedTime;
    @BindView(R.id.view_pager_transaction)
    ViewPager mViewPager;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    protected CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.tab_name)
    FontTextView tabName;

    @BindView(R.id.tab_indicator)
    TabLayout tabIndicator;

    @BindView(R.id.not_confirmed_flag)
    protected
    FontTextView notConfFlag;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    private final static String POSITION = "position";

    private TransactionFragmentPresenterImpl mTransactionFragmentPresenter;

    public static BaseFragment newInstance(Context context, int position) {
        BaseFragment transactionFragment = Factory.instantiateFragment(context, TransactionFragment.class);
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        transactionFragment.setArguments(args);
        return transactionFragment;
    }

    @Override
    protected void createPresenter() {
        mTransactionFragmentPresenter = new TransactionFragmentPresenterImpl(this);
    }

    @Override
    protected TransactionFragmentPresenterImpl getPresenter() {
        return mTransactionFragmentPresenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().openTransactionView(getArguments().getInt(POSITION));
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
    }

    protected void setTransactionData(String value, String receivedTime){
        if(mViewPager.getAdapter() == null) {
            mTextViewValue.setText(value);

            mTextViewReceivedTime.setText(receivedTime);
            TransactionPagerAdapter transactionPagerAdapter = new TransactionPagerAdapter(getFragmentManager());
            mViewPager.setAdapter(transactionPagerAdapter);
            tabIndicator.setupWithViewPager(mViewPager, true);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
                @Override
                public void onPageScrollStateChanged(int state) {}

                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
                        tabName.setText(getString(R.string.from));
                    } else {
                        tabName.setText(getString(R.string.to));
                    }
                }
            });
        }
    }


    private class TransactionPagerAdapter extends FragmentStatePagerAdapter{

        public TransactionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment transactionDetailFragment = null;
            switch (position){
                case TransactionDetailFragment.ACTION_FROM:{
                    transactionDetailFragment =  TransactionDetailFragment.newInstance(getContext(), TransactionDetailFragment.ACTION_FROM,
                            getArguments().getInt(POSITION));
                    break;
                }
                case TransactionDetailFragment.ACTION_TO:{
                    transactionDetailFragment = TransactionDetailFragment.newInstance(getContext(), TransactionDetailFragment.ACTION_TO,
                            getArguments().getInt(POSITION));
                    break;
                }
            }
            return transactionDetailFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
