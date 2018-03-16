package org.qtum.wallet.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

import org.qtum.wallet.R;

public class CustomProgressBarView extends android.support.v7.widget.AppCompatImageView {

    int i;
    int count;
    Bitmap bitmap;
    int backgroundColor;
    int progressSrc;
    Drawable vectorDrawable;

    public CustomProgressBarView(Context context) {
        super(context);
    }

    public CustomProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SearchBar,
                0, 0);
        progressSrc = R.drawable.ic_progress_dark_1;
        try {
            progressSrc = a.getResourceId(R.styleable.CustomProgressBarView_progressSrc, R.drawable.ic_progress_dark_1);
        } finally {
            a.recycle();
        }

        vectorDrawable = ContextCompat.getDrawable(getContext(), progressSrc);

        bitmap = Bitmap.createBitmap(getResources().getDisplayMetrics().widthPixels + vectorDrawable.getIntrinsicWidth() * 2,
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        i = count = -vectorDrawable.getIntrinsicWidth();
        int count = getResources().getDisplayMetrics().widthPixels / vectorDrawable.getIntrinsicWidth() + 2;
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        vectorDrawable.draw(canvas);
        for (int i = 0; i < count; i++) {
            canvas.translate(vectorDrawable.getIntrinsicWidth(), 0);
            vectorDrawable.draw(canvas);
        }
        backgroundColor = ((ColorDrawable) getBackground()).getColor();
    }

    public CustomProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (count == 0) {
            count = i;
        }
        canvas.drawColor(backgroundColor);
        canvas.drawBitmap(bitmap, count++, 0, null);
        postInvalidate();
    }
}
