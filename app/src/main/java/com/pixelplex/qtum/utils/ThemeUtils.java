package com.pixelplex.qtum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.base_activity.BaseActivity;

/**
 * Created by kirillvolkov on 04.07.17.
 */

public class ThemeUtils {

    public static final String THEME_KEY = "THEME_KEY";
    public static final String THEME_DARK = "THEME_DARK";
    public static final String THEME_LIGHT = "THEME_LIGHT";
    public static String currentTheme;

    public static String getCurrentTheme(Context context){
        if(currentTheme == null){
           currentTheme = PreferenceManager.getDefaultSharedPreferences(context).getString(THEME_KEY, THEME_DARK);
        }
        return currentTheme;
    }

    public static void setAppTheme(BaseActivity activity, String themeName){
        activity.getApplication().setTheme(getThemeIdByName(themeName));
    }

    public static void setCurrentPreferencesTheme(Context context, String themeName){
        currentTheme = themeName;
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString(THEME_KEY,themeName);
        edit.apply();
    }

    public static void switchPreferencesTheme(Context context) {
        switch (getCurrentTheme(context)){
            case THEME_LIGHT:
                setCurrentPreferencesTheme(context, THEME_DARK);
                break;
            case THEME_DARK:
                setCurrentPreferencesTheme(context, THEME_LIGHT);
                break;
        }
    }

    public static int getThemeIdByName(String themeName){
        switch (themeName){
            case THEME_LIGHT:
                return R.style.AppThemeWhite;
            case THEME_DARK:
                return R.style.AppTheme;
            default:
                throw new Resources.NotFoundException("Theme not found");
        }
    }
}
