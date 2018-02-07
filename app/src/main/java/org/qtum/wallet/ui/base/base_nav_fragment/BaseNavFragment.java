package org.qtum.wallet.ui.base.base_nav_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

/**
 * Created by kirillvolkov on 30.01.2018.
 */

public abstract class BaseNavFragment extends BaseFragment{

    public abstract int getRootView();

    public abstract void activateTab();

    public abstract String getNavigationTag();

    HiddenChangeListener hiddenChangeListener;

    public void setHiddenChangeListener(HiddenChangeListener hiddenChangeListener) {
        this.hiddenChangeListener = hiddenChangeListener;
        this.hiddenChangeListener.onParentHiddenChanged(false);
    }

    public void removeHiddenChangeListener() {
        this.hiddenChangeListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        removeHiddenChangeListener();
    }

    public void clearChildBackStack(){
        FragmentManager fm = getChildFragmentManager();
        if(fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backStackEntryAt = fm.getBackStackEntryAt(0);
            fm.popBackStack(backStackEntryAt.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

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

    @Override
    public void onResume() {
        super.onResume();
        if(getMainActivity().isCurrentNavFragment(getClass().getCanonicalName())) {
            activateTab();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hiddenChangeListener != null){
            hiddenChangeListener.onParentHiddenChanged(hidden);
        }

        if(!hidden){
            activateTab();
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
