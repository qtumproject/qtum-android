package com.pixelplex.qtum.dataprovider.firebase;

import android.content.Context;
import android.content.SharedPreferences;

import com.pixelplex.qtum.dataprovider.firebase.listeners.FireBaseTokenRefreshListener;

public class FirebaseSharedPreferences {

    private final String FIREBASE_DATA_STORAGE = "firebase_data_storage";
    private static FirebaseSharedPreferences sInstance;

    private final String PREV_TOKEN = "prev_token";
    private final String CURRENT_TOKEN = "current_token";

    private FireBaseTokenRefreshListener mFireBaseTokenRefreshListener;

    private FirebaseSharedPreferences(){
    }

    public static FirebaseSharedPreferences getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseSharedPreferences();
        }
        return sInstance;
    }

    public void saveFirebaseToken(Context context, String token){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(FIREBASE_DATA_STORAGE, Context.MODE_PRIVATE);
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
        SharedPreferences mSharedPreferences = context.getSharedPreferences(FIREBASE_DATA_STORAGE, Context.MODE_PRIVATE);
        firebaseTokens[0] = mSharedPreferences.getString(PREV_TOKEN,null);
        firebaseTokens[1] = mSharedPreferences.getString(CURRENT_TOKEN,null);
        return firebaseTokens;
    }

    public void addFirebaseTokenRefreshListener(FireBaseTokenRefreshListener fireBaseTokenRefreshListener){
        mFireBaseTokenRefreshListener = fireBaseTokenRefreshListener;
    }

    public void removeFirebaseTokenRefreshListener(){
        mFireBaseTokenRefreshListener = null;
    }

}
