package com.pixelplex.qtum.ui.activity.main_activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.dataprovider.listeners.LanguageChangeListener;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.base_activity.BasePresenterImpl;
import com.pixelplex.qtum.ui.fragment.NewsFragment.NewsFragment;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.ProfileFragment;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import com.pixelplex.qtum.ui.fragment.StartPageFragment.StartPageFragment;
import com.pixelplex.qtum.ui.fragment.WalletMainFragment.WalletMainFragment;
import com.pixelplex.qtum.utils.QtumIntent;


class MainActivityPresenterImpl extends BasePresenterImpl implements MainActivityPresenter {

    private MainActivityView mMainActivityView;
    private MainActivityInteractorImpl mMainActivityInteractor;
    private Fragment mRootFragment;
    private Context mContext;
    public boolean mAuthenticationFlag = false;
    private boolean mCheckAuthenticationFlag = false;
    private boolean mCheckAuthenticationShowFlag = false;
    private boolean mSendFromIntent = false;

    private Intent mIntent;
    private UpdateService mUpdateService;

    private NetworkStateReceiver mNetworkReceiver;

    private String mAddressForSendAction;
    private String mAmountForSendAction;

    private LanguageChangeListener mLanguageChangeListener;

    MainActivityPresenterImpl(MainActivityView mainActivityView) {
        mMainActivityView = mainActivityView;
        mContext = getView().getContext();
        mMainActivityInteractor = new MainActivityInteractorImpl(mContext);
    }

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);

        mNetworkReceiver = new NetworkStateReceiver(getView().getNetworkConnectedFlag());
        mContext.registerReceiver(mNetworkReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mLanguageChangeListener = new LanguageChangeListener() {
            @Override
            public void onLanguageChange() {
                getView().resetMenuText();
            }
        };

        QtumSharedPreference.getInstance().addLanguageListener(mLanguageChangeListener);

    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        if(mAuthenticationFlag && !mCheckAuthenticationShowFlag){
            mCheckAuthenticationFlag = true;
        }
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        if(mCheckAuthenticationFlag && !mCheckAuthenticationShowFlag){
            PinFragment pinFragment = PinFragment.newInstance(PinFragment.CHECK_AUTHENTICATION);
            getView().openFragment(pinFragment);
            mCheckAuthenticationShowFlag = true;
        }
    }

    public boolean isCheckAuthenticationShowFlag() {
        return mCheckAuthenticationShowFlag;
    }

    public void setCheckAuthenticationShowFlag(boolean checkAuthenticationShowFlag) {
        mCheckAuthenticationShowFlag = checkAuthenticationShowFlag;
    }

    @Override
    public MainActivityView getView() {
        return mMainActivityView;
    }

    private MainActivityInteractorImpl getInteractor() {
        return mMainActivityInteractor;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mUpdateService = ((UpdateService.UpdateBinder) iBinder).getService();
            mUpdateService.clearNotification();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public UpdateService getUpdateService(){
        return mUpdateService;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPostCreate(Context contex) {
        super.onPostCreate(contex);

        mIntent = new Intent(mContext, UpdateService.class);
        if (!isMyServiceRunning(UpdateService.class)) {
            mContext.startService(mIntent);
        }
        mContext.bindService(mIntent,mServiceConnection,0);

        openStartFragment();

    }

    private void openStartFragment() {
        Fragment fragment;
        if (getInteractor().getKeyGeneratedInstance(mContext)) {
            if(mSendFromIntent){
                fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION_AND_SEND);
                getView().openRootFragment(fragment);
            } else {
                fragment = StartPageFragment.newInstance(true);
                getView().openRootFragment(fragment);
            }
        } else {
            fragment = StartPageFragment.newInstance(false);
            getView().openRootFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_wallet:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(WalletMainFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = WalletMainFragment.newInstance();
                break;
            case R.id.item_profile:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(ProfileFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = ProfileFragment.newInstance();
                break;
            case R.id.item_news:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(NewsFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = NewsFragment.newInstance();
                break;
            case R.id.item_send:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(SendBaseFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = SendBaseFragment.newInstance(false,null,null);
                break;
            default:
                return false;
        }
        getView().openRootFragment(mRootFragment);
        return true;
    }

    @Override
    public void setRootFragment(Fragment fragment) {
        mRootFragment = fragment;
    }

    @Override
    public void processIntent(Intent intent) {
        switch (intent.getAction()){
            case QtumIntent.SEND_FROM_SDK:
                mSendFromIntent = true;
                mAddressForSendAction = intent.getStringExtra(QtumIntent.SEND_ADDRESS);
                mAmountForSendAction = intent.getStringExtra(QtumIntent.SEND_AMOUNT);
                break;
            case NfcAdapter.ACTION_NDEF_DISCOVERED:
                mSendFromIntent = true;
                mAddressForSendAction = "QbShaLBf1nAX3kznmGU7vM85HFRYJVG6ut";
                mAmountForSendAction = "1.431";
//                if(mAuthenticationFlag) {
//                    mRootFragment = SendBaseFragment.newInstance(false, mAddressForSendAction, mAmountForSendAction);
//                    getView().setIconChecked(3);
//                } else{
//                    mRootFragment = PinFragment.newInstance(PinFragment.AUTHENTICATION_AND_SEND);
//
//                }
//                getView().openRootFragment(mRootFragment);
                break;
            default:
                break;
        }
    }

    @Override
    public void processNewIntent(Intent intent) {
        switch (intent.getAction()) {
            case QtumIntent.OPEN_FROM_NOTIFICATION:
                mRootFragment = WalletMainFragment.newInstance();
                getView().openRootFragment(mRootFragment);
                getView().setIconChecked(0);
                break;
            case QtumIntent.SEND_FROM_SDK:
                mAddressForSendAction = intent.getStringExtra(QtumIntent.SEND_ADDRESS);
                mAmountForSendAction = intent.getStringExtra(QtumIntent.SEND_AMOUNT);
                if(mAuthenticationFlag){
                    mRootFragment = SendBaseFragment.newInstance(false,mAddressForSendAction,mAmountForSendAction);
                    getView().openRootFragment(mRootFragment);
                    getView().setIconChecked(3);
                } else {
                    Fragment fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION_AND_SEND);
                    getView().openRootFragment(fragment);
                }
                break;
            case NfcAdapter.ACTION_NDEF_DISCOVERED:
                mAddressForSendAction = "QbShaLBf1nAX3kznmGU7vM85HFRYJVG6ut";
                mAmountForSendAction = "0.253";
                if(mAuthenticationFlag) {
                    mRootFragment = SendBaseFragment.newInstance(false, mAddressForSendAction, mAmountForSendAction);
                    getView().setIconChecked(3);
                } else{
                    mRootFragment = PinFragment.newInstance(PinFragment.AUTHENTICATION_AND_SEND);

                }
                getView().openRootFragment(mRootFragment);
                break;
            default:
                break;
        }
    }

    @Override
    public NetworkStateReceiver getNetworkReceiver() {
        return mNetworkReceiver;
    }

    @Override
    public void onDestroy(Context context) {
        super.onDestroy(context);
        mContext.unbindService(mServiceConnection);
        getInteractor().clearStatic();
        mContext.unregisterReceiver(mNetworkReceiver);
    }

    public void setAuthenticationFlag(boolean authenticationFlag) {
        mAuthenticationFlag = authenticationFlag;
    }

    public String getAddressForSendAction() {
        return mAddressForSendAction;
    }

    public String getAmountForSendAction() {
        return mAmountForSendAction;
    }
}