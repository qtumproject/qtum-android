package org.qtum.wallet.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class StackCollapseLinearLayout extends FrameLayout {

    private static final int[] DRAW_ORDER = new int[]{2, 1, 0};

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return DRAW_ORDER[i];
    }

    public StackCollapseLinearLayout(Context context) {
        super(context);
    }

    public StackCollapseLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
        InitialHeight = getDefaultheight(context);
    }

    public StackCollapseLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
        InitialHeight = getDefaultheight(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StackCollapseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setChildrenDrawingOrderEnabled(true);
        InitialHeight = getDefaultheight(context);
    }

    private float InitialHeight = -1;
    private float prevHeight = 0;

    public float getInitialHeight() {
        return InitialHeight;
    }

    float prevpercents = 1;

    private ArrayList<Float> childYPositions = new ArrayList<>();

    public void collapseFromPercents(float percents) {
        //if(Math.abs(prevpercents - percents) < 0.2 && Math.abs(prevpercents - percents) > 0) {
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
//            params.height = (int) (InitialHeight * percents);
//            setLayoutParams(params);
        //}
       // prevpercents = percents;
        ResizeHeightAnimation heightAnimation = new ResizeHeightAnimation(this, getHeight(), (int) (InitialHeight * percents));
        heightAnimation.setDuration(0);
        startAnimation(heightAnimation);
    }

    private void setChildPosition(View child, float delta) {
        child.setTranslationY(child.getY() - delta);
    }

    private void setChildPositionDelta(View child, float delta) {
        child.setTranslationY(delta);
    }

    public static float getDefaultheight(final Context context) {
        return 150 * context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (childYPositions.size() == 0) {

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                childYPositions.add(child.getTranslationY());
            }
        }

        if (prevHeight > b - t) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getY() + child.getHeight() > getHeight()) {
                    setChildPosition(child, (child.getY() + child.getHeight()) - (getHeight()));
                }
            }
        } else {
            if (childYPositions.size() != 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (childYPositions.get(i) > child.getY()) {
                        float pos = getHeight() - child.getHeight();
                        if (i == 0) {
                            if (pos <= 0) {
                                setChildPositionDelta(child, pos);
                            } else {
                                setChildPositionDelta(child, 0);
                            }
                        } else {
                            if (pos <= getChildAt(i - 1).getY() + getChildAt(i - 1).getHeight()) {
                                setChildPositionDelta(child, pos);
                            } else {
                                setChildPositionDelta(child, getChildAt(i - 1).getY() + getChildAt(i - 1).getHeight());
                            }
                        }
                        break;
                    }
                }
            }
        }
        prevHeight = b - t;

        super.onLayout(changed, l, t, r, b);
    }
}
