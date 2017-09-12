package org.qtum.wallet.ui.fragment.set_your_token_fragment.dark;

import android.view.View;

import org.qtum.wallet.ui.fragment.set_your_token_fragment.InputViewHolder;
import org.qtum.wallet.ui.fragment.set_your_token_fragment.OnValidateParamsListener;
import org.qtum.wallet.utils.FontManager;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class InputViewHolderDark extends InputViewHolder {

    public InputViewHolderDark(View itemView, OnValidateParamsListener listener) {
        super(itemView, listener);
        tilParam.setTypeface(FontManager.getInstance().getFont(tilParam.getContext().getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        etParam.setTypeface(FontManager.getInstance().getFont(etParam.getContext().getString(org.qtum.wallet.R.string.simplonMonoRegular)));
    }
}
