package org.qtum.wallet.datastorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.utils.migration_manager.KeystoreMigrationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QtumSharedPreference {
    private static QtumSharedPreference sInstance = null;

    private final String QTUM_DATA_STORAGE = "qtum_data_storage";
    private final String QTUM_PASSWORD = "qtum_wallet_password";
    private final String QTUM_SIX_DIGIT_PASSWORD = "qtum_wallet_six_digit_password";
    private final String QTUM_IS_KEY_GENERATED = "qtum_is_key_generated";
    private final String QTUM_SEED = "qtum_seed";
    private final String TOUCH_ID_ENABLE = "touch_id_enable";
    private final String TOUCH_ID_PASSWORD = "touch_id_password";
    private final String BAN_TIME = "ban_time";
    private final String FAILED_ATTEMPTS_COUNT = "failed_attempts_count";
    private final String MIN_GAS_PRICE = "min_gas_price";
    private final String CURRENT_ADDRESS = "current_active_address";
    private final String BALANCE_STRING = "balance_string";
    private final String UNCONFIRMED_BALANCE_STRING = "unconfirmed_balance_string";
    private final String LAST_UPDATED_BALANCE_TIME = "last_updated_balance_time";

    private static String passphrase_migration_state = "passphrase_migration_state";

    private QtumSharedPreference() {

    }

    public static QtumSharedPreference getInstance() {
        if (sInstance == null) {
            sInstance = new QtumSharedPreference();
        }
        return sInstance;
    }

    public String getKeystoreMigrationState(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(passphrase_migration_state
                , null);
    }

    public void setKeyStoreMigrationState(Context context, String state) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(passphrase_migration_state, state);
        mEditor.apply();
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

    public void saveCurrentAddress(Context context, String address) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(CURRENT_ADDRESS, address);
        mEditor.apply();
    }

    public String getCurrentAddress(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(CURRENT_ADDRESS, null);
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
        String passphraseMigrationState = mSharedPreferences.getString(passphrase_migration_state,null);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();

        mEditor.putString(passphrase_migration_state, passphraseMigrationState); //for prevent lost flag
        mEditor.apply();
    }

    public void forceClear(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public void setMinGasPrice(Context context, Integer minGasLimit) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt(MIN_GAS_PRICE, minGasLimit);
        mEditor.apply();
    }

    public Integer getMinGasPrice(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getInt(MIN_GAS_PRICE, 40);
    }

    public void setBalanceString(Context context, String balance){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(BALANCE_STRING, balance);
        mEditor.apply();
    }

    public String getBalanceString(Context context){
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(BALANCE_STRING,"0");
    }

    public void setUnconfirmedBalanceString(Context context, String balance){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(UNCONFIRMED_BALANCE_STRING, balance);
        mEditor.apply();
    }

    public String getUnconfirmedBalanceString(Context context){
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getString(UNCONFIRMED_BALANCE_STRING,"0");
    }

    public void setLastUpdatedBalanceTime(Context context, Long lastUpdatedBalanceTime) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong(LAST_UPDATED_BALANCE_TIME, lastUpdatedBalanceTime);
        mEditor.apply();
    }

    public Long getLastUpdatedBalanceTime(Context context) {
        return context.getSharedPreferences(QTUM_DATA_STORAGE, Context.MODE_PRIVATE).getLong(LAST_UPDATED_BALANCE_TIME, 0);
    }

}
