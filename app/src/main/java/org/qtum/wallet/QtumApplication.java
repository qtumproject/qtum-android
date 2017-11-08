package org.qtum.wallet;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import org.qtum.wallet.datastorage.QStoreStorage;
import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.model.Version;
import org.qtum.wallet.utils.FontManager;
import org.qtum.wallet.utils.MigrationManager;

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

        try {
            int currentVersion = getCodeVersion();
            QtumSettingSharedPreference qtumSettingSharedPreference = new QtumSettingSharedPreference();
            int migrationVersion = qtumSettingSharedPreference.getCodeVersion(getApplicationContext());
            if(currentVersion>migrationVersion){
                MigrationManager migrationManager = new MigrationManager();
                int newMigrationVersion = migrationManager.makeMigration(currentVersion,migrationVersion, getApplicationContext());
                qtumSettingSharedPreference.setMigrationCodeVersionString(getApplicationContext(), newMigrationVersion);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getCodeVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        return pInfo.versionCode;
    }
}