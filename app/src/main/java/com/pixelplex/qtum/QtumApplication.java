package com.pixelplex.qtum;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.pixelplex.qtum.utils.FontManager;

import io.fabric.sdk.android.*;

/**
 * Created by kirillvolkov on 16.05.17.
 */

public class QtumApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        FontManager.init(getAssets());
    }
}