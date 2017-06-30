package com.pixelplex.qtum;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.pixelplex.qtum.utils.FontManager;

import io.fabric.sdk.android.*;

public class QtumApplication extends MultiDexApplication {

    private boolean contractAwait = false;

    @Override
    public void onCreate() {
        super.onCreate();

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        FontManager.init(getAssets());
    }

    public boolean isContractAwait() {
        return contractAwait;
    }

    public void setContractAwait(boolean contractAwait) {
        this.contractAwait = contractAwait;
    }
}