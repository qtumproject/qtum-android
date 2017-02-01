package org.qtum.mromanovsky.qtum.datastorage;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;


public class QtumSharedPreference {
    private static QtumSharedPreference sInstance = null;

    private static final String QTUM_DATA_STORAGE = "qtum_data_storage";
    private static final String QTUM_WALLET_NAME = "qtum_wallet_name";
    private static final String QTUM_WALLET_PASSWORD = "qtum_wallet_password";
    private static final String QTUM_IS_KEY_GENERATED = "qtum_is_key_generated";
    private static final String QTUM_KEY_IDENTIFIER = "qtum_key_identifier";

    private QtumSharedPreference() { }

    public static QtumSharedPreference getInstance() {
        if (sInstance == null) {
            sInstance = new QtumSharedPreference();
        }
        return sInstance;
    }

    public void saveWalletName(Context context, String name) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_WALLET_NAME, name);
        mEditor.apply();
    }

    public String getWalletName(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_WALLET_NAME, "");
    }

    public void saveWalletPassword(Context context, int password) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(QTUM_WALLET_PASSWORD, password);
        mEditor.apply();
    }

    public int getWalletPassword(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getInt(QTUM_WALLET_PASSWORD, 0);
    }

    public void setKeyGeneratedInstance(Context context, boolean isKeyGenerated) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(QTUM_IS_KEY_GENERATED, isKeyGenerated);
        mEditor.apply();
    }

    public boolean getKeyGeneratedInstance(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getBoolean(QTUM_IS_KEY_GENERATED, false);
    }

    public String getIdentifier(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_KEY_IDENTIFIER, "");
    }

    public void saveIdentifier(Context context, String identifier) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_KEY_IDENTIFIER, identifier);
        mEditor.apply();
    }

    public void clear(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
        File file = new File(context.getFilesDir().getPath().toString() + "/key_storage");
        file.delete();
    }
}
