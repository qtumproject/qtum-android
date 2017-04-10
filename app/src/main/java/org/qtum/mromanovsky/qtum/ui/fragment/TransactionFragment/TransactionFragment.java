package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment.TransactionDetailFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarFragment.WalletAppBarFragment;

import java.text.DecimalFormat;
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
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    final static String POSITION = "position";

    TransactionFragmentPresenterImpl mTransactionFragmentPresenter;

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
        TabLayout.Tab tabFrom = mTabLayout.newTab();
        TabLayout.Tab tabTo = mTabLayout.newTab();
        tabFrom.setText("From");
        tabTo.setText("To");
        mTabLayout.addTab(tabFrom);
        mTabLayout.addTab(tabTo);
    }

    @Override
    public void setUpTransactionData(String value, String receivedTime, List<String> from, List<String> to) {

        mTextViewValue.setText(value);

        mTextViewReceivedTime.setText(receivedTime);


        if (Double.valueOf(value) > 0) {
            mAppBarLayout.setBackgroundResource(R.drawable.background_tb_sent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getFragmentActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.green));
            }
        } else {
            mAppBarLayout.setBackgroundResource(R.drawable.background_tb_received);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getFragmentActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.pink_lite));
            }
        }

        TransactionPagerAdapter transactionPagerAdapter = new TransactionPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(transactionPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
