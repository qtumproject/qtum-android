package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;


public class MainActivityPresenterImpl extends BasePresenterImpl implements MainActivityPresenter{

    MainActivityView mMainActivityView;

    public MainActivityPresenterImpl(MainActivityView mainActivityView){
        mMainActivityView = mainActivityView;
    }


    @Override
    public BaseContextView getView() {
        return mMainActivityView;
    }
}
