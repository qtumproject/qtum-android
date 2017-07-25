package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.Dark;

import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.InputViewHolder;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.OnValidateParamsListener;
import com.pixelplex.qtum.utils.FontManager;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class InputViewHolderDark extends InputViewHolder {

    public InputViewHolderDark(View itemView, OnValidateParamsListener listener) {
        super(itemView, listener);
        tilParam.setTypeface(FontManager.getInstance().getFont(tilParam.getContext().getString(R.string.simplonMonoRegular)));
        etParam.setTypeface(FontManager.getInstance().getFont(etParam.getContext().getString(R.string.simplonMonoRegular)));
    }
}
