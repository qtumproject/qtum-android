package com.pixelplex.qtum.ui.activity.main_activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.base_activity.BaseActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.SendIBeaconPresenter;
import com.pixelplex.qtum.utils.CustomContextWrapper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements MainActivityView{

    private static final int REQV_PERM_CODE = 1488;
    private static final int LAYOUT = R.layout.activity_main;
    private MainActivityPresenterImpl mMainActivityPresenterImpl;
    private NfcAdapter mNfcAdapter;
    private ActivityResultListener mActivityResultListener;
    private PermissionsResultListener mPermissionsResultListener;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    private SendIBeaconPresenter iBeaconPresenter;

    @Override
    protected void createPresenter() {
        mMainActivityPresenterImpl = new MainActivityPresenterImpl(this);
        iBeaconPresenter = new SendIBeaconPresenter(this);
    }

    @Override
    protected MainActivityPresenterImpl getPresenter() {
        return mMainActivityPresenterImpl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        //TODO: NFC
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter!=null && mNfcAdapter.isEnabled()) {
            String s = getIntent().getAction();
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(s)) {
                Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                NdefMessage[] msgs = null;
                if (rawMsgs != null) {
                    msgs = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        msgs[i] = (NdefMessage) rawMsgs[i];
                    }
                }
                buildTagViews(msgs);
            }
        }

//        if(Build.VERSION.SDK_INT >= 23){
//            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != GRANTED){
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},REQV_PERM_CODE);
//            } else {
//                iBeaconPresenter.initMonitoring();
//            }
//        } else {
//            iBeaconPresenter.initMonitoring();
//        }

    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter!=null && mNfcAdapter.isEnabled()) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            final PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            IntentFilter[] filters = new IntentFilter[1];
            String[][] techList = new String[][]{};

            // Notice that this is the same filter as in our manifest.
            filters[0] = new IntentFilter();
            filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filters[0].addCategory(Intent.CATEGORY_DEFAULT);
            try {
                filters[0].addDataType("text/plain");
            } catch (IntentFilter.MalformedMimeTypeException e) {
                throw new RuntimeException("Check your mime type.");
            }

            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!=null && mNfcAdapter.isEnabled()) {
            mNfcAdapter.disableForegroundDispatch(this);
        }

    }
    //TODO: NFC END

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().processNewIntent(intent);
    }

    public void setAuthenticationFlag(boolean authenticationFlag){
        getPresenter().setAuthenticationFlag(authenticationFlag);
    }

    public String getAddressForSendAction(){
        return getPresenter().getAddressForSendAction();
    }

    public String getAmountForSendAction(){
        return getPresenter().getAmountForSendAction();
    }

    public void loadPermissions(String perm, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
    }

    public boolean checkPermission(String perm){
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void openRootFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(BaseFragment.BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void openFragment(Fragment fragment) {
        hideKeyBoard();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    public UpdateService getUpdateService(){
        return getPresenter().getUpdateService();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack(BaseFragment.BACK_STACK_ROOT_TAG, 0);
    }

    @Override
    public void setIconChecked(int position) {
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void resetMenuText() {
        int[] menuResources = new int[]{R.string.wallet,R.string.profile,R.string.news,R.string.send};
        Menu menu = mBottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setTitle(getResources().getString(menuResources[i]));
        }
    }

    @Override
    public boolean getNetworkConnectedFlag() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void setAdressAndAmount(String defineMinerAddress, String defineAmount) {
        if(getSupportFragmentManager().findFragmentByTag(SendBaseFragment.class.getCanonicalName()) == null && getPresenter().mAuthenticationFlag) {
            openRootFragment(SendBaseFragment.newInstance(false, defineMinerAddress, defineAmount));
        }
    }

    public boolean checkTouchId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(QtumSharedPreference.getInstance().isTouchIdEnable(getContext())) {
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                return checkPermission(Manifest.permission.USE_FINGERPRINT) && fingerprintManager.isHardwareDetected();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public void showBottomNavigationView(boolean recolorStatusBar) {
        mBottomNavigationView.setVisibility(View.VISIBLE);

        if(recolorStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            }
        }
    }

    public void hideBottomNavigationView(boolean recolorStatusBar) {
        mBottomNavigationView.setVisibility(View.GONE);

        if(recolorStatusBar) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.background));
            }
        }
    }

    @Override
    public void initializeViews() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return getPresenter().onNavigationItemSelected(item);
            }
        });

        getPresenter().processIntent(getIntent());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mActivityResultListener!=null){
            mActivityResultListener.onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mPermissionsResultListener!=null) {
            mPermissionsResultListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if(requestCode == REQV_PERM_CODE){
            if(grantResults[0] >= 0 && grantResults[1] >= 0){
                iBeaconPresenter.initMonitoring();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setRootFragment(Fragment rootFragment) {
        getPresenter().setRootFragment(rootFragment);
    }

    public NetworkStateReceiver getNetworkReceiver(){
        return getPresenter().getNetworkReceiver();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CustomContextWrapper.wrap(newBase, QtumSharedPreference.getInstance().getLanguage(newBase)));
    }

    public QtumApplication getQtumApplication(){
        return (QtumApplication)getApplication();
    }

    public void addActivityResultListener(ActivityResultListener activityResultListener){
        mActivityResultListener = activityResultListener;
    }

    public void removeResultListener(){
        mActivityResultListener = null;
    }

    public void addPermissionResultListener(PermissionsResultListener permissionsResultListener){
        mPermissionsResultListener = permissionsResultListener;
    }

    public void removePermissionResultListener(){
        mPermissionsResultListener = null;
    }

    public interface ActivityResultListener{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public interface PermissionsResultListener{
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1 || getPresenter().isCheckAuthenticationShowFlag()) {
            ActivityCompat.finishAffinity(this);
        }else {
            super.onBackPressed();
        }
    }

    public void setCheckAuthenticationShowFlag(boolean flag){
        getPresenter().setCheckAuthenticationShowFlag(flag);
    }
}