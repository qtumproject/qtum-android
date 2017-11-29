package org.qtum.wallet.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

public class FontTextInputEditText extends TextInputEditText {
    public FontTextInputEditText(Context context) {
        this(context, null);
    }

    public FontTextInputEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode())
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, org.qtum.wallet.R.styleable.FontTextInputEditText);

        if (ta != null) {
            String fontAsset = ta.getString(org.qtum.wallet.R.styleable.FontTextInputEditText_inputTypeface);

            if (!TextUtils.isEmpty(fontAsset)) {
                Typeface tf = FontManager.getInstance().getFont(fontAsset);
                int style = Typeface.NORMAL;

                if (getTypeface() != null)
                    style = getTypeface().getStyle();

                if (tf != null)
                    setTypeface(tf, style);
                else
                    Log.d("FontText", String.format("Could not create a font from asset: %s", fontAsset));
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(focused){
            int i = 2;
        }
    }
}