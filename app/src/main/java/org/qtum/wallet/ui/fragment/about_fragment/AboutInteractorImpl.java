package org.qtum.wallet.ui.fragment.about_fragment;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AboutInteractorImpl implements AboutInteractor{

    private Context mContext;

    AboutInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public String getVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        return pInfo.versionName;
    }

    @Override
    public int getCodeVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        return pInfo.versionCode;
    }
}
