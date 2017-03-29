package org.qtum.mromanovsky.qtum.datastorage;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;


public class QtumSharedPreference {
    private static QtumSharedPreference sInstance = null;

    private final String QTUM_DATA_STORAGE = "qtum_data_storage";
    private final String QTUM_WALLET_NAME = "qtum_wallet_name";
    private final String QTUM_WALLET_PASSWORD = "qtum_wallet_password";
    private final String QTUM_IS_KEY_GENERATED = "qtum_is_key_generated";
    private final String QTUM_KEY_IDENTIFIER = "qtum_key_identifier";
    private final String QTUM_SEED = "qtum_seed";
    private final String QTUM_EXCHANGE_RATES = "qtum_exchange_rates";

    private QtumSharedPreference() {
    }

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

    public void saveExchangeRates(Context context, double exchangeRates) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_EXCHANGE_RATES, String.valueOf(exchangeRates));
        mEditor.apply();
    }

    public double getExchangeRates(Context context) {
        return Double.parseDouble(context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_EXCHANGE_RATES, "1"));
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

    public String getSeed(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_SEED, "");
    }

    void saveSeed(Context context, String seed) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_SEED, seed);
        mEditor.apply();
    }

    public void clear(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
        File file = new File(context.getFilesDir().getPath() + "/key_storage");
        file.delete();
    }
}
