package com.pixelplex.qtum.ui.fragment.LanguageFragment.Light;

import android.util.Pair;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.LanguageFragment.Dark.LanguageAdapterDark;
import com.pixelplex.qtum.ui.fragment.LanguageFragment.LanguageFragment;

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
