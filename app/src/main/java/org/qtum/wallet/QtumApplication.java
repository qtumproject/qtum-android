package org.qtum.wallet;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;

import org.qtum.wallet.datastorage.QStoreStorage;
import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.utils.FontManager;
import org.qtum.wallet.utils.migration_manager.MigrationManager;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class QtumApplication extends MultiDexApplication{

    public static QtumApplication instance;
    public static final String REALM_NAME = "org.qtum.realm";
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
        FontManager.init(getAssets());
        QStoreStorage.getInstance(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(REALM_NAME)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        try {
            int currentVersion = getCodeVersion();
            QtumSettingSharedPreference qtumSettingSharedPreference = QtumSettingSharedPreference.getInstance();
            int migrationVersion = qtumSettingSharedPreference.getCodeVersion(getApplicationContext());
            if (currentVersion > migrationVersion) {
                MigrationManager migrationManager = new MigrationManager();
                int newMigrationVersion = migrationManager.makeMigration(currentVersion, migrationVersion, getApplicationContext());
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

    public Realm getRealm() {
        return realm;
    }
}