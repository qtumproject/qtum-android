package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarFragment.WalletAppBarFragment;


public class WalletAppBarPagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public WalletAppBarPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return WalletAppBarFragment.newInstance();
    }

    //TODO: edit
    @Override
    public int getCount() {
        return 3;
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
