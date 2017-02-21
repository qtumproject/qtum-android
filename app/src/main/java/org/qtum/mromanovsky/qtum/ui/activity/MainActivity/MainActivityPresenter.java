package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;


import android.support.v4.app.Fragment;

interface MainActivityPresenter {
    void openFragment(Fragment fragment,Fragment currentFragment);
}
