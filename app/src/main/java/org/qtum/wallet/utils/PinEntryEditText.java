package org.qtum.wallet.utils;

import android.content.Context;
import android.graphics.RectF;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Locale;

public class PinEntryEditText extends com.alimuzaffar.lib.pin.PinEntryEditText {

    public PinEntryEditText(Context context) {
        super(context);
    }

    public PinEntryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        super.setFilters(filters);

        mMaxLength = getMaxLength(filters);
        mNumChars = mMaxLength;
        if (mMaxLength > 0) {
            int availableWidth = getWidth() - ViewCompat.getPaddingEnd(this) - ViewCompat.getPaddingStart(this);
            if (mSpace < 0) {
                mCharSize = (availableWidth / (mNumChars * 2 - 1));
            } else {
                mCharSize = (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
            }
            mLineCoords = new RectF[(int) mNumChars];
            mCharBottom = new float[(int) mNumChars];
            int startX;
            int bottom = getHeight() - getPaddingBottom();
            int rtlFlag;
            final boolean isLayoutRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL;
            if (isLayoutRtl) {
                rtlFlag = -1;
                startX = (int) (getWidth() - ViewCompat.getPaddingStart(this) - mCharSize);
            } else {
                rtlFlag = 1;
                startX = ViewCompat.getPaddingStart(this);
            }
            for (int i = 0; i < mNumChars; i++) {
                mLineCoords[i] = new RectF(startX, bottom, startX + mCharSize, bottom);
                if (mPinBackground != null) {
                    if (mIsDigitSquare) {
                        mLineCoords[i].top = getPaddingTop();
                        mLineCoords[i].right = startX + mLineCoords[i].height();
                    } else {
                        mLineCoords[i].top -= mTextHeight.height() + mTextBottomPadding * 2;
                    }
                }

                if (mSpace < 0) {
                    startX += rtlFlag * mCharSize * 2;
                } else {
                    startX += rtlFlag * (mCharSize + mSpace);
                }
                mCharBottom[i] = mLineCoords[i].bottom - mTextBottomPadding;
            }
        }
        invalidate();
    }

    public int getMaxLength(InputFilter[] filters) {
        int maxLength = -1;
        for (InputFilter filter : filters) {
            if (filter instanceof InputFilter.LengthFilter) {
                try {
                    Field maxLengthField = filter.getClass().getDeclaredField("mMax");
                    maxLengthField.setAccessible(true);

                    if (maxLengthField.isAccessible()) {
                        maxLength = maxLengthField.getInt(filter);
                    }
                } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException e) {
                    Log.w(filter.getClass().getName(), e);
                }
            }
        }
        return maxLength;
    }
}
