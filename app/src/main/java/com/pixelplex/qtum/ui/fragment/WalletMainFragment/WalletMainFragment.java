package com.pixelplex.qtum.ui.fragment.WalletMainFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.OtherTokens.OtherTokensFragment;
import com.pixelplex.qtum.ui.fragment.WalletFragment.WalletFragment;

import butterknife.BindView;


public abstract class WalletMainFragment extends BaseFragment implements WalletMainFragmentView {

    private WalletFragment mWalletFragment;
    private OtherTokensFragment mOtherTokensFragment;

    public static WalletMainFragment newInstance(Context context) {
        Bundle args = new Bundle();
        WalletMainFragment fragment = (WalletMainFragment) Factory.instantiateFragment(context, WalletMainFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    private WalletMainFragmentPresenterImpl mWalletMainFragmentPresenter;

    @BindView(R.id.view_pager)
    protected
    ViewPager pager;

    @Override
    protected void createPresenter() {
        mWalletMainFragmentPresenter = new WalletMainFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mWalletMainFragmentPresenter;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        pager.setAdapter(new FragmentAdapter(getFragmentManager()));
        showBottomNavView(false);
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {

        int NUM_ITEMS = 1;

        public void showOtherTokens(boolean show){
            NUM_ITEMS = (show)? 2 : 1;
            notifyDataSetChanged();
        }

        public FragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mWalletFragment = (WalletFragment) WalletFragment.newInstance(getContext());
                    return mWalletFragment;
                case 1:
                    mOtherTokensFragment = (OtherTokensFragment) OtherTokensFragment.newInstance(getContext());
                    return mOtherTokensFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWalletFragment!=null){
            mWalletFragment.dismiss();
        }
        if(mOtherTokensFragment !=null){
            mOtherTokensFragment.dismiss();
        }
    }
}
