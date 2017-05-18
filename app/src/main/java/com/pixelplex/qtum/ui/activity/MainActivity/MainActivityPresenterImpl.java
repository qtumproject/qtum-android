package com.pixelplex.qtum.ui.activity.MainActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.datastorage.LanguageChangeListener;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.BaseActivity.BasePresenterImpl;
import com.pixelplex.qtum.ui.fragment.NewsFragment.NewsFragment;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;
import com.pixelplex.qtum.ui.fragment.ProfileFragment.ProfileFragment;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import com.pixelplex.qtum.ui.fragment.StartPageFragment.StartPageFragment;
import com.pixelplex.qtum.ui.fragment.WalletFragment.WalletFragment;
import com.pixelplex.qtum.utils.QtumIntent;


class MainActivityPresenterImpl extends BasePresenterImpl implements MainActivityPresenter {

    private MainActivityView mMainActivityView;
    private MainActivityInteractorImpl mMainActivityInteractor;
    private Fragment mRootFragment;
    private Context mContext;

    private boolean mAuthenticationFlag = false;
    private boolean mSendFromSdkFlag = false;

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

        mNetworkReceiver = new NetworkStateReceiver();
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
            if(mSendFromSdkFlag){
                fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION_AND_SEND);
                getView().openRootFragment(fragment);
            } else {
                fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION);
                getView().openRootFragment(fragment);
            }
        } else {
            fragment = StartPageFragment.newInstance();
            getView().openRootFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_wallet:
                if (mRootFragment != null && mRootFragment.getClass().getCanonicalName().equals(WalletFragment.class.getCanonicalName())) {
                    getView().popBackStack();
                    return true;
                }
                mRootFragment = WalletFragment.newInstance();
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
                mSendFromSdkFlag = true;
                mAddressForSendAction = intent.getStringExtra(QtumIntent.SEND_ADDRESS);
                mAmountForSendAction = intent.getStringExtra(QtumIntent.SEND_AMOUNT);
                break;
            default:
                break;
        }
    }

    @Override
    public void processNewIntent(Intent intent) {
        switch (intent.getAction()) {
            case QtumIntent.OPEN_FROM_NOTIFICATION:
                mRootFragment = WalletFragment.newInstance();
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