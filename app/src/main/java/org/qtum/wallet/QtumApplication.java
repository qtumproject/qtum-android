package org.qtum.wallet;

import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import org.qtum.wallet.datastorage.QStoreStorage;
import org.qtum.wallet.utils.FontManager;

import io.fabric.sdk.android.Fabric;

public class QtumApplication extends MultiDexApplication{

    public static QtumApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if(!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        FontManager.init(getAssets());
        QStoreStorage.getInstance(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}