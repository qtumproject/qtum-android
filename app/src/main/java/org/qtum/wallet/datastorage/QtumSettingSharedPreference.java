package org.qtum.wallet.datastorage;

import android.content.Context;
import android.content.SharedPreferences;

public class QtumSettingSharedPreference {

    private final String QTUM_SETTING = "qtum_setting";
    private final String SHOW_CONTRACTS_DELETE_UNSUBSCRIBE_WIZARD = "show_contracts_delete_unsubscribe_wizard";
    private final String MIGRATION_VERSION_STRING = "migration_version_string";

    public void setShowContractsDeleteUnsubscribeWizard(Context context, boolean isShow) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(SHOW_CONTRACTS_DELETE_UNSUBSCRIBE_WIZARD, isShow);
        mEditor.apply();
    }

    public boolean getShowContractsDeleteUnsubscribeWizard(Context context) {
        return context.getSharedPreferences(QTUM_SETTING, Context.MODE_PRIVATE).getBoolean(SHOW_CONTRACTS_DELETE_UNSUBSCRIBE_WIZARD, true);
    }

    public void setMigrationCodeVersionString(Context context, int codeVersion){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MIGRATION_VERSION_STRING, codeVersion);
        mEditor.apply();
    }

    public int getCodeVersion(Context context){
        return context.getSharedPreferences(QTUM_SETTING, Context.MODE_PRIVATE).getInt(MIGRATION_VERSION_STRING, 0);
    }
}
