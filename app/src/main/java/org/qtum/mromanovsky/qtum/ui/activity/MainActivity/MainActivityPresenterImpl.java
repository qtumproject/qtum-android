package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.NewsFragment.NewsFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment.ProfileFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment.StartPageFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;


class MainActivityPresenterImpl extends BasePresenterImpl implements MainActivityPresenter {

    private MainActivityView mMainActivityView;
    private MainActivityInteractorImpl mMainActivityInteractor;
    private Fragment mRootFragment;

    MainActivityPresenterImpl(MainActivityView mainActivityView) {
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
        if (getInteractor().getKeyGeneratedInstance(getView().getContext())) {
            fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION);
            getView().openRootFragment(fragment);
        } else {
            fragment = StartPageFragment.newInstance();
            getView().openRootFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_wallet:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(WalletFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = WalletFragment.newInstance();
                break;
            case R.id.item_profile:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(ProfileFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = ProfileFragment.newInstance();
                break;
            case R.id.item_news:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(NewsFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = NewsFragment.newInstance();
                break;
            case R.id.item_send:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(SendBaseFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = SendBaseFragment.newInstance(false);
                break;
            default:
                return false;
        }
        getView().openRootFragment(mRootFragment);
        return true;
    }

    @Override
    public void setRootFragment(Fragment fragment) {
        mRootFragment = fragment;
    }

    @Override
    public void processIntent(Intent intent) {
        if(intent.getBooleanExtra("notification_action", false)){
            mRootFragment = WalletFragment.newInstance();
            getView().openRootFragment(mRootFragment);
            getView().setIconChecked(0);
        }
    }
}