package org.qtum.mromanovsky.qtum.datastorage;

import android.content.Context;
import android.content.SharedPreferences;


public class QtumSharedPreference {
    private static QtumSharedPreference sInstance = null;

    private static final String QTUM_DATA_STORAGE = "qtum_data_storage";
    private static final String QTUM_WALLET_NAME = "qtum_wallet_name";
    private static final String QTUM_WALLET_PASSWORD = "qtum_wallet_password";
    private static final String QTUM_ADDRESS = "qtum_address";
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

    public void saveAddress(Context context, String address) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_ADDRESS, address);
        mEditor.apply();
    }

    public String getAddress(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_ADDRESS, "");
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
    }
}
