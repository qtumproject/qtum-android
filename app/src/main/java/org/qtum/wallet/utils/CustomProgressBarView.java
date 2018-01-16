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
    }

    public CustomProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Drawable vectorDrawable =  ContextCompat.getDrawable(getContext(), progressSrc);
        i = count = -vectorDrawable.getIntrinsicWidth();
        Bitmap bitmap = Bitmap.createBitmap(getWidth() + vectorDrawable.getIntrinsicWidth()*2,
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        int count = getWidth() / vectorDrawable.getIntrinsicWidth() + 2;
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        vectorDrawable.draw(canvas);
        for (int i = 0; i < count; i++) {
            canvas.translate(vectorDrawable.getIntrinsicWidth(), 0);
            vectorDrawable.draw(canvas);
        }
        this.bitmap = bitmap;
        backgroundColor = ((ColorDrawable) getBackground()).getColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (count == 0) {
            count = i;
        }
        canvas.drawColor(backgroundColor);
        canvas.drawBitmap(bitmap, count++, 0, new Paint());
        invalidate();
    }
}
