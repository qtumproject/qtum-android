package org.qtum.wallet.ui.fragment.about_fragment;


import android.content.pm.PackageManager;

interface AboutInteractor {

    String getVersion() throws PackageManager.NameNotFoundException;
    int getCodeVersion() throws PackageManager.NameNotFoundException;

}
