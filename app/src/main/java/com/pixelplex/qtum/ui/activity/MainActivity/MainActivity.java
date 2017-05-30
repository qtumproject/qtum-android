package com.pixelplex.qtum.ui.activity.MainActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
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

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.BaseActivity.BaseActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.ContractConfirmFragment.CompleteDialogFragment;
import com.pixelplex.qtum.utils.CustomContextWrapper;

import java.lang.reflect.Field;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements MainActivityView {

    private static final int LAYOUT = R.layout.activity_main;
    private MainActivityPresenterImpl mMainActivityPresenterImpl;
    private static final int REQUEST_CAMERA = 0;
    private NfcAdapter mNfcAdapter;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void createPresenter() {
        mMainActivityPresenterImpl = new MainActivityPresenterImpl(this);
    }

    @Override
    protected MainActivityPresenterImpl getPresenter() {
        return mMainActivityPresenterImpl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);
    }

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

    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
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


    public void showBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        }
    }

    public void hideBottomNavigationView() {
        mBottomNavigationView.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.background));
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

    Dialog processingDialog;

    public void showProcessing() {
        if(processingDialog == null) {
            processingDialog = new Dialog(this);
            processingDialog.setContentView(R.layout.lyt_processing_dialog);
        }
        processingDialog.show();
    }

    public void hideProcessing() {
        if(processingDialog != null && processingDialog.isShowing()) {
            processingDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(processingDialog != null) {
            processingDialog.dismiss();
        }
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
}