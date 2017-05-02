package org.qtum.mromanovsky.qtum.ui.fragment.LanguageFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

import java.util.ArrayList;
import java.util.List;

class LanguageFragmentInteractor {
    private Context mContext;
    private List<String> mLanguagesList;

    LanguageFragmentInteractor(Context context){
        mContext = context;
        mLanguagesList = new ArrayList<>();
        mLanguagesList.add("default");
        mLanguagesList.add("ru");
    }

    public String getLanguage(){
        return  QtumSharedPreference.getInstance().getLanguage(mContext);
    }

    public void setLanguage(String language){
        QtumSharedPreference.getInstance().saveLanguage(mContext, language);
    }

    public List<String> getLanguagesList(){
        return mLanguagesList;
    }

}