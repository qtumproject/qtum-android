package com.pixelplex.qtum.utils.appbarbehaviour;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pixelplex.qtum.utils.FontTextView;


class BalanceBehaviour extends CoordinatorLayout.Behavior<FontTextView> {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FontTextView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FontTextView child, View dependency) {
        child.setY(dependency.getY());
        return true;
    }

}
