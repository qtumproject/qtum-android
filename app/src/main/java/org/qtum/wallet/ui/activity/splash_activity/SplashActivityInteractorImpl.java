package org.qtum.wallet.ui.activity.splash_activity;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.FeePerKb;

import rx.Observable;


class SplashActivityInteractorImpl implements SplashActivityInteractor {

    private Context mContext;

    SplashActivityInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public Observable<DGPInfo> getDGPInfo() {
        return QtumService.newInstance().getDGPInfo();
    }

    @Override
    public Observable<FeePerKb> getFeePerKb() {
        return QtumService.newInstance().getEstimateFeePerKb(2);
    }
}
