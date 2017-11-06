package org.qtum.wallet.datastorage;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.qtum.wallet.model.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QtumSettingSharedPreference {

    private final String QTUM_SETTING = "qtum_setting";
    private final String SHOW_CONTRACTS_DELETE_UNSUBSCRIBE_WIZARD = "show_contracts_delete_unsubscribe_wizard";
    private final String APPLICATION_VERSION = "app_version";
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

    public void setMigrationVersionString(Context context, List<Version> versions){
        Gson gson = new Gson();
        ArrayList<String> versionsStringArray = new ArrayList<>();
        for (Version version : versions) {
            versionsStringArray.add(gson.toJson(version));
        }
        putListString(MIGRATION_VERSION_STRING,versionsStringArray,context);
    }

    private void putListString(String key, ArrayList<String> stringList, Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        mEditor.putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public List<Version> getVersions(Context context){
        Gson gson = new Gson();

        ArrayList<String> versionsStringArray = getListString(MIGRATION_VERSION_STRING, context);
        ArrayList<Version> versions = new ArrayList<>();
        for (String versionString : versionsStringArray) {
            Version version = gson.fromJson(versionString, Version.class);
            if (version != null) {
                versions.add(version);
            }
        }

        return versions;
    }

    private ArrayList<String> getListString(String key, Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_SETTING, Context.MODE_PRIVATE);
        return new ArrayList<>(Arrays.asList(TextUtils.split(mSharedPreferences.getString(key, ""), "‚‗‚")));
    }

}
