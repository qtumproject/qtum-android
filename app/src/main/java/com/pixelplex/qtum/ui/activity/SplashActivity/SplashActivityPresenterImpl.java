package com.pixelplex.qtum.ui.activity.SplashActivity;

import android.content.Context;
import android.os.Handler;

import com.pixelplex.qtum.SmartContractsManager.StorageManager;
import com.pixelplex.qtum.ui.activity.BaseActivity.BasePresenterImpl;

/**
 * Created by kirillvolkov on 16.05.17.
 */

public class SplashActivityPresenterImpl extends BasePresenterImpl implements SplashActivityPresenter {

    private SplashActivityView mMainActivityView;
    private SplashActivityInteractorImpl mMainActivityInteractor;

    private Context mContext;

    SplashActivityPresenterImpl(SplashActivityView mainActivityView) {
        mMainActivityView = mainActivityView;
        mContext = getView().getContext();
        mMainActivityInteractor = new SplashActivityInteractorImpl(mContext);
        handler = new Handler();
        StorageManager.getInstance().migrateDefaultContracts(mContext);
    }

    Handler handler;

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().startApp();
            }
        },3000);
    }

    @Override
    public SplashActivityView getView() {
        return mMainActivityView;
    }
}
