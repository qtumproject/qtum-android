package org.qtum.wallet.ui.fragment.wallet_main_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.other_tokens.OtherTokensFragment;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletFragment;

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

    @BindView(org.qtum.wallet.R.id.view_pager)
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

    @Override
    public void showOtherTokens(boolean isShow) {
        if(pager.getAdapter() != null) {
            ((FragmentAdapter) pager.getAdapter()).showOtherTokens(isShow);
        }
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {

        int NUM_ITEMS = 1;

        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

       public WalletFragment getWalletFragment(){
            return (WalletFragment) registeredFragments.get(0);
        }

        OtherTokensFragment getOtherTokensFragment(){
            return (OtherTokensFragment) registeredFragments.get(1);
        }

        public void showOtherTokens(boolean show){
            NUM_ITEMS = (show)? 2 : 1;
            notifyDataSetChanged();
            if(show){
                showPageIndicator();
                getOtherTokensFragment().notifyTokenChange();
            }else{
                hidePageIndicator();
            }
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
