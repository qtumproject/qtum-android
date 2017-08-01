package com.pixelplex.qtum;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import com.pixelplex.qtum.utils.FontManager;
import io.fabric.sdk.android.Fabric;

public class QtumApplication extends MultiDexApplication {

    public static QtumApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        FontManager.init(getAssets());

    }
}