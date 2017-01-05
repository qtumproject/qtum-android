package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment.StartPageFragment;
import org.qtum.mromanovsky.qtum.utils.QtumSharedPreference;


public class MainActivityPresenterImpl extends BasePresenterImpl implements MainActivityPresenter{

    MainActivityView mMainActivityView;
    Fragment mFragment;

    public MainActivityPresenterImpl(MainActivityView mainActivityView){
        mMainActivityView = mainActivityView;
    }

    @Override
    public MainActivityView getView() {
        return mMainActivityView;
    }

    @Override
    public void openStartFragment() {
        if(QtumSharedPreference.getInstance().getWalletName(getView().getContext()).isEmpty()){
            mFragment = StartPageFragment.newInstance();
            getView().openFragment(mFragment);
        } else {
            mFragment = PinFragment.newInstance(false);
            getView().openFragment(mFragment);
        }
    }

    @Override
    public void openFragment(Fragment fragment) {
        getView().openFragment(fragment);
    }
}
