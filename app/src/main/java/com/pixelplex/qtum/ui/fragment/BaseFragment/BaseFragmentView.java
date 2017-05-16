package com.pixelplex.qtum.ui.fragment.BaseFragment;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pixelplex.qtum.ui.activity.BaseActivity.BaseContextView;


public interface BaseFragmentView extends BaseContextView {
    void openRootFragment(Fragment fragment);
    void openFragment(Fragment fragment);
    Activity getFragmentActivity();
    void setProgressDialog(String message);
    void dismissProgressDialog();
    void setAlertDialog(String message);
    void dismissAlertDialog();
    void showSoftInput();
    void setFocusTextInput(View textInputEditText, View textInputLayout);
}
