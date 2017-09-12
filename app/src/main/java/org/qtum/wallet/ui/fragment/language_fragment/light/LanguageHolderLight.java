package org.qtum.wallet.ui.fragment.language_fragment.light;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.ui.fragment.language_fragment.LanguageHolder;
import org.qtum.wallet.ui.fragment.language_fragment.OnLanguageIntemClickListener;

/**
 * Created by kirillvolkov on 07.07.17.
 */

public class LanguageHolderLight extends LanguageHolder {
    public LanguageHolderLight(View itemView, OnLanguageIntemClickListener listener) {
        super(itemView, listener);
    }

    public void bindLanguage(Pair<String, String> language) {
        if (language.first.equals(QtumSharedPreference.getInstance().getLanguage(mTextViewLanguage.getContext()))) {
            mImageViewCheckIndicator.setVisibility(View.VISIBLE);
            mLinearLayoutAddress.setBackgroundColor(ContextCompat.getColor(mTextViewLanguage.getContext(), R.color.qr_code_background));
        } else {
            mImageViewCheckIndicator.setVisibility(View.GONE);
            mLinearLayoutAddress.setBackgroundColor(Color.TRANSPARENT);
        }
        mTextViewLanguage.setText(language.second);
    }
}
