package com.pixelplex.qtum.ui.activity.splash_activity;

import android.content.Context;
import android.os.Handler;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.ui.base.base_activity.BasePresenterImpl;


class SplashActivityPresenterImpl extends BasePresenterImpl implements SplashActivityPresenter {

    private SplashActivityView mMainActivityView;
    private SplashActivityInteractorImpl mMainActivityInteractor;

    private Context mContext;

    private Handler handler;

    SplashActivityPresenterImpl(SplashActivityView mainActivityView) {
        mMainActivityView = mainActivityView;
        mContext = getView().getContext();
        mMainActivityInteractor = new SplashActivityInteractorImpl(mContext);
        handler = new Handler();
        FileStorageManager.getInstance().migrateDefaultContracts(mContext);
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().startApp();
            }
        },2000);
    }

    @Override
    public SplashActivityView getView() {
        return mMainActivityView;
    }
}
