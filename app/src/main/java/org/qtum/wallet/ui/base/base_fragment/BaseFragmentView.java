package org.qtum.wallet.ui.base.base_fragment;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import org.qtum.wallet.ui.base.base_activity.BaseContextView;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;


public interface BaseFragmentView extends BaseContextView {
    void openRootFragment(Fragment fragment);
    void openFragment(Fragment fragment);
    void openFragmentWithBackStack(Fragment fragment, String tag);
    void openFragmentForResult(Fragment fragment);
    MainActivity getMainActivity();
    Fragment getFragment();
    void setProgressDialog();
    void dismissProgressDialog();
    void setAlertDialog(String title,String buttonText, BaseFragment.PopUpType type);
    void setAlertDialog(String title, String message,String buttonText, BaseFragment.PopUpType type);
    void setAlertDialog(String title, String message, String buttonText, BaseFragment.PopUpType type, BaseFragment.AlertDialogCallBack callBack);
    void dismissAlertDialog();
    void showSoftInput();
    void setFocusTextInput(View textInputEditText, View textInputLayout);
    void hideBottomNavView(boolean recolorStatusBar);
    void showBottomNavView(boolean recolorStatusBar);
    void dismiss();
    FragmentManager getFragmentManager();
}
