package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;


import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenter;

public interface MainActivityPresenter extends BasePresenter{
    void openStartFragment();
    void openFragment(Fragment fragment);
}
