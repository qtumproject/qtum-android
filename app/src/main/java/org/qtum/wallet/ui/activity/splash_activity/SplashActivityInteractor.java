package org.qtum.wallet.ui.activity.splash_activity;


import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.FeePerKb;

import rx.Observable;

interface SplashActivityInteractor {
    Observable<FeePerKb> getFeePerKb();
    Observable<DGPInfo> getDGPInfo();
}
