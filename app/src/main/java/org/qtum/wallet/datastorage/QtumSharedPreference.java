package org.qtum.wallet.datastorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class QtumSharedPreference {
    private static QtumSharedPreference sInstance = null;

    private final String QTUM_DATA_STORAGE = "qtum_data_storage";
    private final String QTUM_PASSWORD = "qtum_wallet_password";
    private final String QTUM_SIX_DIGIT_PASSWORD = "qtum_wallet_six_digit_password";
    private final String QTUM_IS_KEY_GENERATED = "qtum_is_key_generated";
    private final String QTUM_LANGUAGE = "qtum_language";
    private final String QTUM_SEED = "qtum_seed";
    private final String TOUCH_ID_ENABLE = "touch_id_enable";
    private final String TOUCH_ID_PASSWORD = "touch_id_password";
    private final String BAN_TIME = "ban_time";
    private final String FAILED_ATTEMPTS_COUNT = "failed_attempts_count";

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

    public void saveTouchIdEnable(Context context, boolean isEnable) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(TOUCH_ID_ENABLE, isEnable);
        mEditor.apply();
    }

    public boolean isTouchIdEnable(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getBoolean(TOUCH_ID_ENABLE, false);
    }

    public void savePassword(Context context, String password) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_PASSWORD, password);
        mEditor.apply();
    }

    public String getPassword(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_PASSWORD, "");
    }

    public void setBanTime(Context context, Long banTime) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong(BAN_TIME, banTime);
        mEditor.apply();
    }

    public Long getBanTime(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getLong(BAN_TIME, 0);
    }

    public void setFailedAttemptsCount(Context context, Integer failedAttemptsCount) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(FAILED_ATTEMPTS_COUNT, failedAttemptsCount);
        mEditor.apply();
    }

    public Integer getFailedAttemptsCount(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getInt(FAILED_ATTEMPTS_COUNT, 0);
    }

    public void saveSixDigitPassword(Context context, String password) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_SIX_DIGIT_PASSWORD, password);
        mEditor.apply();
    }

    public String getSixDigitPassword(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_SIX_DIGIT_PASSWORD, "");
    }

    public void saveTouchIdPassword(Context context, String password) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(TOUCH_ID_PASSWORD, password);
        mEditor.apply();
    }

    public String getTouchIdPassword(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(TOUCH_ID_PASSWORD, "");
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

    public String getLanguage(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_LANGUAGE, "us");
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

    public void saveSeed(Context context, String seed) {
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
    }

    public void addLanguageListener(LanguageChangeListener languageChangeListener){
        mLanguageChangeListeners.add(languageChangeListener);
    }

    public void removeLanguageListener(LanguageChangeListener languageChangeListener){
        mLanguageChangeListeners.remove(languageChangeListener);
    }
}
