package org.qtum.wallet.ui.activity.main_activity;

import android.support.v4.app.Fragment;

import org.qtum.wallet.ui.base.base_activity.BaseContextView;


public interface MainActivityView extends BaseContextView {
    void openRootFragment(Fragment fragment);
    void popBackStack();
    void setIconChecked(int position);
    void resetMenuText();
    boolean getNetworkConnectedFlag();
    void setAdressAndAmount(String defineMinerAddress, String defineAmount, String tokenAddress);
    void openFragment(Fragment fragment);
    MainActivity getActivity();
    String getQtumAction();
    void showToast(String s);
}
