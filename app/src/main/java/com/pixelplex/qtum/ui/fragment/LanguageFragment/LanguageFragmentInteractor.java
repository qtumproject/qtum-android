package com.pixelplex.qtum.ui.fragment.LanguageFragment;


import android.content.Context;
import android.util.Pair;

import com.pixelplex.qtum.datastorage.QtumSharedPreference;

import java.util.ArrayList;
import java.util.List;

class LanguageFragmentInteractor {
    private Context mContext;
    private List<Pair<String,String>> mLanguagesList;

    LanguageFragmentInteractor(Context context){
        mContext = context;
        mLanguagesList = new ArrayList<>();
        mLanguagesList.add(new Pair<>("us", "English"));
        mLanguagesList.add(new Pair<>("zh", "Chinese"));
    }

    public String getLanguage(){
        return  QtumSharedPreference.getInstance().getLanguage(mContext);
    }

    public void setLanguage(String language){
        QtumSharedPreference.getInstance().saveLanguage(mContext, language);
    }

    public List<Pair<String,String>> getLanguagesList(){
        return mLanguagesList;
    }

}