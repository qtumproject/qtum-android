package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarPagerAdapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.qtum.mromanovsky.qtum.datastorage.TokenList;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarFragment.WalletAppBarFragment;


public class WalletAppBarPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public WalletAppBarPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return WalletAppBarFragment.newInstance(position-1);
    }

    //TODO: edit
    @Override
    public int getCount() {
        return TokenList.getTokenList().getList().size()+1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public WalletAppBarFragment getRegisteredFragment(int position) {
        return (WalletAppBarFragment) registeredFragments.get(position);
    }

}
