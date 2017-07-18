package com.pixelplex.qtum.ui.fragment.TransactionFragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.TransactionDetailFragment;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class TransactionFragment extends BaseFragment implements TransactionFragmentView {

    @BindView(R.id.tv_value)
    TextView mTextViewValue;
    @BindView(R.id.tv_received_time)
    TextView mTextViewReceivedTime;
    @BindView(R.id.view_pager_transaction)
    ViewPager mViewPager;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.tab_name)
    FontTextView tabName;

    @BindView(R.id.tab_indicator)
    TabLayout tabIndicator;

    @BindView(R.id.not_confirmed_flag)
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

    public static TransactionFragment newInstance(int position) {
        TransactionFragment transactionFragment = new TransactionFragment();
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
    protected int getLayout() {
        return R.layout.fragment_transaction;
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

    @Override
    public void setUpTransactionData(String value, String receivedTime, List<String> from, List<String> to, boolean confirmed) {
        if(mViewPager.getAdapter() == null) {
            mTextViewValue.setText(value);
            mTextViewReceivedTime.setText(receivedTime);
            TransactionPagerAdapter transactionPagerAdapter = new TransactionPagerAdapter(getFragmentManager());
            mViewPager.setAdapter(transactionPagerAdapter);
            tabIndicator.setupWithViewPager(mViewPager, true);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 0) {
                        tabName.setText(getString(R.string.from));
                    } else {
                        tabName.setText(getString(R.string.to));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            notConfFlag.setVisibility((!confirmed) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    class TransactionPagerAdapter extends FragmentStatePagerAdapter{

        public TransactionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TransactionDetailFragment transactionDetailFragment = null;
            switch (position){
                case TransactionDetailFragment.ACTION_FROM:{
                    transactionDetailFragment =  TransactionDetailFragment.newInstance(TransactionDetailFragment.ACTION_FROM,
                            getArguments().getInt(POSITION));
                    break;
                }
                case TransactionDetailFragment.ACTION_TO:{
                    transactionDetailFragment = TransactionDetailFragment.newInstance(TransactionDetailFragment.ACTION_TO,
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
