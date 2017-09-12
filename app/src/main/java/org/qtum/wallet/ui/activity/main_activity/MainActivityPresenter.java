package org.qtum.wallet.ui.activity.main_activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;

interface MainActivityPresenter {
    boolean onNavigationItemSelected(@NonNull MenuItem item);
    void setRootFragment(Fragment fragment);
    void processIntent(Intent intent);
    void processNewIntent(Intent intent);
    NetworkStateReceiver getNetworkReceiver();
    void onLogin();
    void onLogout();
    void subscribeOnServiceConnectionChangeEvent(MainActivity.OnServiceConnectionChangeListener listener);
}
