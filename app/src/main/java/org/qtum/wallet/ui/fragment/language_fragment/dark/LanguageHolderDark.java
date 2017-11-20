package org.qtum.wallet.ui.fragment.language_fragment.dark;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.ui.fragment.language_fragment.LanguageHolder;
import org.qtum.wallet.ui.fragment.language_fragment.OnLanguageIntemClickListener;

public class LanguageHolderDark extends LanguageHolder {
    public LanguageHolderDark(View itemView, OnLanguageIntemClickListener listener) {
        super(itemView, listener);
    }

    public void bindLanguage(Pair<String, String> language) {
        if (language.first.equals(QtumSharedPreference.getInstance().getLanguage(mTextViewLanguage.getContext()))) {
            mImageViewCheckIndicator.setVisibility(View.VISIBLE);
            mTextViewLanguage.setTextColor(ContextCompat.getColor(mTextViewLanguage.getContext(), R.color.background));
            mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(mTextViewLanguage.getContext(), R.color.accent_red_color));
        } else {
            mTextViewLanguage.setTextColor(ContextCompat.getColor(mTextViewLanguage.getContext(), R.color.colorPrimary));
            mImageViewCheckIndicator.setVisibility(View.GONE);
            mLinearLayoutAddress.setBackgroundColor(Color.TRANSPARENT);
        }
        mTextViewLanguage.setText(language.second);
    }
}
