package com.pixelplex.qtum.datastorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.pixelplex.qtum.dataprovider.listeners.FireBaseTokenRefreshListener;
import com.pixelplex.qtum.dataprovider.listeners.LanguageChangeListener;

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
    private final String QTUM_LANGUAGE = "qtum_language";
    private final String QTUM_SEED = "qtum_seed";
    private final String PREV_TOKEN = "prev_token";
    private final String CURRENT_TOKEN = "current_token";
    private final String TOUCH_ID_ENABLE = "touch_id_enable";
    private final String TOUCH_ID_PASSWORD = "touch_id_password";

    private List<LanguageChangeListener> mLanguageChangeListeners;
    private FireBaseTokenRefreshListener mFireBaseTokenRefreshListener;

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

    public void saveTouchIdEnable(Context context, boolean isEnable) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(TOUCH_ID_ENABLE, isEnable);
        mEditor.apply();
    }

    public boolean isTouchIdEnable(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getBoolean(TOUCH_ID_ENABLE, true);
    }

    public void saveFirebaseToken(Context context, String token){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String prevToken = mSharedPreferences.getString(CURRENT_TOKEN,null);
        mEditor.putString(PREV_TOKEN,prevToken);
        mEditor.putString(CURRENT_TOKEN, token);
        mEditor.apply();
        if(mFireBaseTokenRefreshListener != null) {
            mFireBaseTokenRefreshListener.onRefresh(prevToken, token);
        }
    }

    public String[] getFirebaseTokens(Context context){
        String[] firebaseTokens = new String[2];
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        firebaseTokens[0] = mSharedPreferences.getString(PREV_TOKEN,null);
        firebaseTokens[1] = mSharedPreferences.getString(CURRENT_TOKEN,null);
        return firebaseTokens;
    }

    public void saveWalletPassword(Context context, String password) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(QTUM_WALLET_PASSWORD, password);
        mEditor.apply();
    }

    public String getWalletPassword(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(QTUM_WALLET_PASSWORD, "");
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

    public void addFirebaseTokenRefreshListener(FireBaseTokenRefreshListener fireBaseTokenRefreshListener){
        mFireBaseTokenRefreshListener = fireBaseTokenRefreshListener;
    }

    public void removeFirebaseTokenRefreshListener(){
        mFireBaseTokenRefreshListener = null;
    }
}
