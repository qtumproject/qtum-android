package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment.StartPageFragment;


public class MainActivityPresenterImpl extends BasePresenterImpl implements MainActivityPresenter {

    private MainActivityView mMainActivityView;
    private MainActivityInteractorImpl mMainActivityInteractor;

    public MainActivityPresenterImpl(MainActivityView mainActivityView) {
        mMainActivityView = mainActivityView;
        mMainActivityInteractor = new MainActivityInteractorImpl();
    }

    @Override
    public MainActivityView getView() {
        return mMainActivityView;
    }

    private MainActivityInteractorImpl getInteractor() {
        return mMainActivityInteractor;
    }

    @Override
    public void onPostCreate(Context contex) {
        super.onPostCreate(contex);
        openStartFragment();
    }

    private void openStartFragment() {
        Fragment fragment;
        if (getInteractor().getWalletPassword(getView().getContext()) == 0) {
            fragment = StartPageFragment.newInstance();
            getView().openFragment(fragment);
        } else {
            fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION);
            getView().openFragment(fragment);
        }
    }

    @Override
    public void openFragment(Fragment fragment) {
        getView().openFragment(fragment);
    }
}