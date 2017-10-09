package org.qtum.wallet.ui.activity.splash_activity;

import android.content.Context;
import android.os.Handler;

import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.QtumNetworkState;
import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.FeePerKb;
import org.qtum.wallet.ui.base.base_activity.BasePresenterImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class SplashActivityPresenterImpl extends BasePresenterImpl implements SplashActivityPresenter {

    private SplashActivityView mMainActivityView;
    private SplashActivityInteractorImpl mMainActivityInteractor;

    private Context mContext;

    SplashActivityPresenterImpl(SplashActivityView mainActivityView) {
        mMainActivityView = mainActivityView;
        mContext = getView().getContext();
        mMainActivityInteractor = new SplashActivityInteractorImpl(mContext);
        FileStorageManager.getInstance().migrateDefaultContracts(mContext);
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        getInteractor().getDGPInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DGPInfo>() {
                    @Override
                    public void onCompleted() {
                        getInteractor().getFeePerKb()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<FeePerKb>() {
                                    @Override
                                    public void onCompleted() {
                                        getView().startApp();
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(FeePerKb feePerKb) {
                                        QtumNetworkState.newInstance().setFeePerKb(feePerKb);
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DGPInfo dgpInfo) {
                        QtumNetworkState.newInstance().setDGPInfo(dgpInfo);
                    }
                });
    }

    @Override
    public SplashActivityView getView() {
        return mMainActivityView;
    }

    public SplashActivityInteractorImpl getInteractor(){
        return mMainActivityInteractor;
    }
}
