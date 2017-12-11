package org.qtum.wallet.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.qtum.wallet.entity.HeaderData;
import org.qtum.wallet.entity.History;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.qtum.wallet.entity.HeaderData.INITIAL_BALANCE;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public class DataStorage {

    private static final String STORAGE = "QTUM_STORAGE";
    private static final String LAST_HISTORY = "LAST_HISTORY";

    private static final String BALANCE = "LAST_BALANCE";
    private static final String UNC_BALANCE = "UNC_LAST_BALANCE";
    private static final String ADDRESS = "WALLET_ADDRESS";

    public static void saveLastHistory(Context context, String history) {
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(LAST_HISTORY, history);
        edit.apply();
    }

    public static List<History> loadLastHistory(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        String data = preferences.getString(LAST_HISTORY, null);
        if(data == null){
            return new ArrayList<History>();
        } else {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<History>>(){}.getType();
            return gson.fromJson(data,listType);
        }
    }

    public static void setLastBalance(Context context, String balance){
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(BALANCE, balance);
        edit.apply();
    }

    public static void setLastUncBalance(Context context, String balance){
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(UNC_BALANCE, balance);
        edit.apply();
    }

    public static void setAddress(Context context, String address){
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(ADDRESS, address);
        edit.apply();
    }

    public static HeaderData getHeaderData(Context context) {
        HeaderData headerData = new HeaderData();
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        headerData.setBalance(preferences.getString(BALANCE, INITIAL_BALANCE));
        headerData.setUnconfirmedBalance(preferences.getString(UNC_BALANCE, null));
        headerData.setAddress(preferences.getString(ADDRESS, null));
        return headerData;
    }

    public static void setHeaderData(Context context, String balance, String uncBalance, String address) {
        SharedPreferences preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(BALANCE, balance);
        edit.putString(UNC_BALANCE, uncBalance);
        edit.putString(ADDRESS, address);
        edit.apply();
    }
}
