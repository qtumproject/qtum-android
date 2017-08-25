package com.pixelplex.qtum.ui.fragment.set_your_token_fragment.light;

import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.set_your_token_fragment.InputViewHolder;
import com.pixelplex.qtum.ui.fragment.set_your_token_fragment.OnValidateParamsListener;
import com.pixelplex.qtum.utils.FontManager;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class InputViewHolderLight extends InputViewHolder {

    public InputViewHolderLight(View itemView, OnValidateParamsListener listener) {
        super(itemView, listener);
        tilParam.setTypeface(FontManager.getInstance().getFont(tilParam.getContext().getString(R.string.proximaNovaRegular)));
        etParam.setTypeface(FontManager.getInstance().getFont(etParam.getContext().getString(R.string.proximaNovaSemibold)));
    }
}
