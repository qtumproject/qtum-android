package org.qtum.wallet.ui.fragment.language_fragment.light;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.ui.fragment.language_fragment.LanguageAdapter;
import org.qtum.wallet.ui.fragment.language_fragment.OnLanguageIntemClickListener;

import java.util.List;

public class LanguageAdapterLight extends LanguageAdapter {

    protected LanguageAdapterLight(List<Pair<String, String>> languagesList, OnLanguageIntemClickListener listener) {
        super(languagesList, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(org.qtum.wallet.R.layout.item_single_checkable_light, parent, false);
        return new LanguageHolderLight(view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mLanguage = mLanguagesList.get(position);
        ((LanguageHolderLight) holder).bindLanguage(mLanguage);
    }
}
