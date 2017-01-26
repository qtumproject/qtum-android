package org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment;


import android.app.Activity;
import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;


public interface BaseFragmentView extends BaseContextView {
    void openFragment(Fragment fragment);
    void openFragmentAndAddToBackStack(Fragment fragment);
    void openFragmentWithOutPopBackStack(Fragment fragment);
    Activity getFragmentActivity();
    void startAnimation();
    void stopAnimation();
}
