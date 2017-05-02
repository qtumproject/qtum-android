package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;


import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;


interface MainActivityView extends BaseContextView {
    void openRootFragment(Fragment fragment);
    void popBackStack();
    void setIconChecked(int position);
    void resetMenuText();
}
