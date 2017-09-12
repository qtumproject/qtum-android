package org.qtum.wallet.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class StackCollapseLinearLayout extends LinearLayout {

    public StackCollapseLinearLayout(Context context) {
        super(context);
    }

    public StackCollapseLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StackCollapseLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StackCollapseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float InitialHeight = -1;
    private float prevHeight = 0;


    private ArrayList<Float> childYPositions = new ArrayList<>();

    public void collapseFromPercents(float percents) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.height = (int) (InitialHeight * percents);
        setLayoutParams(params);
    }

    private void setChildPosition(View child, float delta) {
        child.setY(child.getY() - delta);
    }

    private void setChildPositionDelta(View child, float delta) {
        child.setY(delta);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        InitialHeight = (InitialHeight < 0)? b - t : InitialHeight;

        super.onLayout(changed, l, t, r, b);

        if(childYPositions.size() == 0) {

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                childYPositions.add(child.getY());
            }
        }

        if(prevHeight > b - t) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child.getY() + child.getHeight() > getHeight()) {
                    setChildPosition(child, (child.getY() + child.getHeight()) - (getHeight()));
                }
            }
        } else {
            if(childYPositions.size() != 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (childYPositions.get(i) > child.getY()) {
                        float pos = getHeight() - child.getHeight();
                        if(i == 0){
                            if(pos <= 0){
                                setChildPositionDelta(child,pos);
                            } else {
                                setChildPositionDelta(child,0);
                            }
                        } else {
                            if(pos <= getChildAt(i - 1).getY() + getChildAt(i - 1).getHeight()){
                                setChildPositionDelta(child,pos);
                            } else {
                                setChildPositionDelta(child,getChildAt(i - 1).getY() + getChildAt(i - 1).getHeight());
                            }
                        }
                        break;
                    }
                }
            }
        }
        prevHeight = b - t;
    }
}
