package com.pixelplex.qtum.datastorage;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TokenSharedPreference {
    private static TokenSharedPreference sTokenSharedPreference = null;

    private final String QTUM_TOKEN_STORAGE = "qtum_token_storage";
    private final String TOKEN_LIST = "token_list";

    private TokenSharedPreference(){

    }

    public static TokenSharedPreference getInstance(){
        if(sTokenSharedPreference == null){
            sTokenSharedPreference = new TokenSharedPreference();
        }
        return sTokenSharedPreference;
    }

    public void addToTokenList(Context context, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(QTUM_TOKEN_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> tokenList = new Gson().fromJson(sharedPreferences.getString(TOKEN_LIST, null), listType);
        if(tokenList==null){
            tokenList = new ArrayList<>();
        }
        tokenList.add(token);
        editor.putString(TOKEN_LIST, new Gson().toJson(tokenList));
        editor.apply();
    }

    public List<String> getTokenList(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(QTUM_TOKEN_STORAGE, Context.MODE_PRIVATE);
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        return new Gson().fromJson(sharedPreferences.getString(TOKEN_LIST, null), listType);
    }

}
