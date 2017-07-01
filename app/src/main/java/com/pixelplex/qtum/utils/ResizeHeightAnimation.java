package com.pixelplex.qtum.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class ResizeHeightAnimation extends Animation{

    private int mStartHeight;
    private int mFinishHeight;
    private View mView;

    public ResizeHeightAnimation(View view, int startHeight, int finishHeight)
    {
        mView = view;
        mStartHeight = startHeight;
        mFinishHeight = finishHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        mView.getLayoutParams().height = mStartHeight + (int) ((mFinishHeight - mStartHeight) * interpolatedTime);
        mView.requestLayout();
    }
}
