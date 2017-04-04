package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

interface MainActivityPresenter {
    boolean onNavigationItemSelected(@NonNull MenuItem item);
    void setRootFragment(Fragment fragment);
    void processIntent(Intent intent);
}
