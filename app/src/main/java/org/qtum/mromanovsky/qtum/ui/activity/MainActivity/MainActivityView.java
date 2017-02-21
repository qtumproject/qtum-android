package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;


import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;


interface MainActivityView extends BaseContextView {
    void openFragment(Fragment fragment,Fragment currentFragment);
}
