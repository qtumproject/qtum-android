package org.qtum.wallet.ui.base.base_nav_fragment;

import android.support.v4.app.Fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

/**
 * Created by kirillvolkov on 30.01.2018.
 */

public abstract class BaseNavFragment extends BaseFragment{

    public abstract int getRootView();

    public abstract void activateTab();

    public abstract String getNavigationTag();

    public void addChild(Fragment fragment) {
        if(getChildFragmentManager().findFragmentByTag(fragment.getClass().getCanonicalName())==null) {
            hideKeyBoard();
            getChildFragmentManager()
                    .beginTransaction()
                    .add(getRootView(), fragment, fragment.getClass().getCanonicalName())
                    .addToBackStack(fragment.getClass().getCanonicalName())
                    .commit();
        }
    }

    public void addChild(Fragment fragment, int container) {
        if(getChildFragmentManager().findFragmentByTag(fragment.getClass().getCanonicalName())==null) {
            hideKeyBoard();
            getChildFragmentManager()
                    .beginTransaction()
                    .add(container, fragment, fragment.getClass().getCanonicalName())
                    .addToBackStack(fragment.getClass().getCanonicalName())
                    .commit();
        }
    }

    public boolean onBackPressed(){
        if(getChildFragmentManager().getBackStackEntryCount() == 0){
            return false;
        }
        getChildFragmentManager().popBackStack();
        return true;
    }

}
