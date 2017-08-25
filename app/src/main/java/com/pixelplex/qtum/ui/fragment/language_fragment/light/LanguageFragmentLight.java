package com.pixelplex.qtum.ui.fragment.language_fragment.light;

import android.util.Pair;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.language_fragment.LanguageFragment;

import java.util.List;

/**
 * Created by kirillvolkov on 07.07.17.
 */

public class LanguageFragmentLight extends LanguageFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_language_light;
    }

    @Override
    public void setUpLanguagesList(List<Pair<String,String>> languagesList) {
        mLanguagesList = languagesList;
        mLanguageAdapter = new LanguageAdapterLight(languagesList, this);
        mRecyclerView.setAdapter(mLanguageAdapter);
    }
}
