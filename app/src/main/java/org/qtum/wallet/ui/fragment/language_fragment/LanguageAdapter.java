package org.qtum.wallet.ui.fragment.language_fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import java.util.List;

/**
 * Created by kirillvolkov on 07.07.17.
 */

public abstract class LanguageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Pair<String,String>> mLanguagesList;
    protected Pair<String,String> mLanguage;

    protected OnLanguageIntemClickListener listener;

    protected LanguageAdapter(List<Pair<String, String>> languagesList, OnLanguageIntemClickListener listener) {
        mLanguagesList = languagesList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mLanguagesList.size();
    }
}