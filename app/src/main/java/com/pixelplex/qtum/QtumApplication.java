package com.pixelplex.qtum;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.pixelplex.qtum.utils.FontManager;

/**
 * Created by kirillvolkov on 16.05.17.
 */

public class QtumApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FontManager.init(getAssets());
    }
}
