package com.pixelplex.qtum.ui.activity.MainActivity;

import android.support.v4.app.Fragment;

import com.pixelplex.qtum.ui.activity.BaseActivity.BaseContextView;


public interface MainActivityView extends BaseContextView {
    void openRootFragment(Fragment fragment);
    void popBackStack();
    void setIconChecked(int position);
    void resetMenuText();
    boolean getNetworkConnectedFlag();

    void setAdressAndAmount(String defineMinerAddress, String defineAmount);
}
