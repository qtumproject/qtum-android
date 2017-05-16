package com.pixelplex.qtum.datastorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class QtumSharedPreference {
    private static QtumSharedPreference sInstance = null;

    private final String QTUM_DATA_STORAGE = "qtum_data_storage";
    private final String QTUM_WALLET_NAME = "qtum_wallet_name";
    private final String QTUM_WALLET_PASSWORD = "qtum_wallet_password";
    private final String QTUM_IS_KEY_GENERATED = "qtum_is_key_generated";
    private final String QTUM_KEY_IDENTIFIER = "qtum_key_identifier";
    private final String QTUM_LANGUAGE = "qtum_language";
    private final String QTUM_SEED = "qtum_seed";
    private final String QTUM_EXCHANGE_RATES = "qtum_exchange_rates";

    private List<LanguageChangeListener> mLanguageChangeListeners;

    private QtumSharedPreference() {
        mLanguageChangeListeners = new ArrayList<>();
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

    public String getLanguage(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_LANGUAGE, "default");
    }

    public void saveLanguage(Context context, String language) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_LANGUAGE, language);
        mEditor.apply();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, null);
        for(LanguageChangeListener languageChangeListener : mLanguageChangeListeners){
            languageChangeListener.onLanguageChange();
        }
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

    public void addLanguageListener(LanguageChangeListener languageChangeListener){
        mLanguageChangeListeners.add(languageChangeListener);
    }

    public void removeLanguageListener(LanguageChangeListener languageChangeListener){
        mLanguageChangeListeners.remove(languageChangeListener);
    }
}
