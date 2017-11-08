package org.qtum.wallet.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import org.qtum.wallet.R;

import java.util.logging.Handler;


public class FontTextView extends android.support.v7.widget.AppCompatTextView {

    Typeface tf;

    public Typeface getTypeface() {
        return tf;
    }

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode())
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);

        if (ta != null) {
            String fontAsset = ta.getString(R.styleable.FontTextView_typefaceAsset);

            if (!TextUtils.isEmpty(fontAsset)) {
                tf = FontManager.getInstance().getFont(fontAsset);
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

    public void setLongNumberText(final String number) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (allowWidthNumber(number)) {
                    setText(number);
                } else {
                    setText(ContractBuilder.getShortBigNumberRepresentation(number));
                }
            }
        });
    }

    public void setLongNumberText(final String number, int allowWidth) {
        if (allowWidthNumber(number, allowWidth)) {
            setText(number);
        } else {
            setText(ContractBuilder.getShortBigNumberRepresentation(number));
        }
    }

    private boolean allowWidthNumber(String number) {
        Paint p = new Paint();
        p.setTypeface(getTypeface()); // if custom font use `TypeFace.createFromFile`
        p.setTextSize(getTextSize());
        return p.measureText(number) < getWidth();
    }

    private boolean allowWidthNumber(String number, int allowWidth) {
        Paint p = new Paint();
        p.setTypeface(getTypeface()); // if custom font use `TypeFace.createFromFile`
        p.setTextSize(getTextSize());
        return p.measureText(number) < allowWidth;
    }

}
