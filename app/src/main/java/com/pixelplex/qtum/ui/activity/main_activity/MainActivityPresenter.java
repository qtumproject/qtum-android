package com.pixelplex.qtum.ui.activity.main_activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.pixelplex.qtum.dataprovider.NetworkStateReceiver;

interface MainActivityPresenter {
    boolean onNavigationItemSelected(@NonNull MenuItem item);
    void setRootFragment(Fragment fragment);
    void processIntent(Intent intent);
    void processNewIntent(Intent intent);
    NetworkStateReceiver getNetworkReceiver();
}
