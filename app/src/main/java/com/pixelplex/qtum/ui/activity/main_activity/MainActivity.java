package com.pixelplex.qtum.ui.activity.main_activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.res.Configuration;
import android.content.res.ColorStateList;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;
import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.services.update_service.UpdateService;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.base.base_activity.BaseActivity;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.wallet_fragment.WalletFragment;
import com.pixelplex.qtum.utils.CustomContextWrapper;
import com.pixelplex.qtum.utils.FontManager;

import com.pixelplex.qtum.ui.fragment.profile_fragment.ProfileFragment;
import com.pixelplex.qtum.ui.fragment.send_fragment.SendFragment;
import com.pixelplex.qtum.utils.ThemeUtils;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements MainActivityView{

    private static final int LAYOUT = R.layout.activity_main;
    private static final int LAYOUT_LIGHT = R.layout.activity_main_light;
    private MainActivityPresenterImpl mMainActivityPresenterImpl;
    private ActivityResultListener mActivityResultListener;
    private PermissionsResultListener mPermissionsResultListener;

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
        setContentView((ThemeUtils.THEME_DARK.equals(ThemeUtils.currentTheme))? LAYOUT : LAYOUT_LIGHT);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().processNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public String getAddressForSendAction(){
        return getPresenter().getAddressForSendAction();
    }

    public String getAmountForSendAction(){
        return getPresenter().getAmountForSendAction();
    }

    public String getTokenForSendAction(){
        return getPresenter().getTokenForSendAction();
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

    @Override
    public String getQtumAction() {
        if(getIntent() != null){
            return getIntent().getAction();
        }
        return null;
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    public UpdateService getUpdateService(){
        return getPresenter().getUpdateService();
    }

    public void subscribeServiceConnectionChangeEvent(OnServiceConnectionChangeListener listener){
        getPresenter().subscribeOnServiceConnectionChangeEvent(listener);
    }

    public interface OnServiceConnectionChangeListener {
        void onServiceConnectionChange(boolean isConnecting);
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
    public void setAdressAndAmount(String defineMinerAddress, String defineAmount, String tokenAddress) {
        if (getSupportFragmentManager().findFragmentByTag(SendFragment.class.getCanonicalName()) == null && getPresenter().mAuthenticationFlag) {
            openRootFragment(SendFragment.newInstance(false, defineMinerAddress, defineAmount, tokenAddress, getContext()));
        }
    }

    public boolean checkTouchIdEnable() {
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

    public boolean checkAvailabilityTouchId(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                return checkPermission(Manifest.permission.USE_FINGERPRINT) && fingerprintManager.isHardwareDetected();
        } else {
            return false;
        }
    }

    public void showBottomNavigationView(boolean recolorStatusBar) {
        if(mBottomNavigationView != null)
            mBottomNavigationView.setVisibility(View.VISIBLE);
        if(recolorStatusBar) {
            recolorStatusBarBlue();
        }
    }

    public void hideBottomNavigationView(boolean recolorStatusBar) {
        if(mBottomNavigationView != null)
            mBottomNavigationView.setVisibility(View.GONE);
        if(recolorStatusBar) {
            recolorStatusBarBlack();
        }
    }

    public void recolorStatusBarBlack(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.background));
        }
    }

    public void recolorStatusBarBlue(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
    }

    public boolean isBottomNavigationViewVisible(){
        return mBottomNavigationView.getVisibility() == View.VISIBLE;
    }

    public void showBottomNavigationView(int resColorId) {
        if(mBottomNavigationView != null)
            mBottomNavigationView.setVisibility(View.VISIBLE);
        if(resColorId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), resColorId));
            }
        }
    }

    public void hideBottomNavigationView(int resColorId) {
        if(mBottomNavigationView != null)
        mBottomNavigationView.setVisibility(View.GONE);
        if(resColorId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), resColorId));
            }
        }
    }

    @Override
    public void initializeViews() {

        initBottomNavViewWithFont((ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)? R.string.simplonMonoRegular : R.string.proximaNovaRegular));

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return getPresenter().onNavigationItemSelected(item);
            }
        });

        getPresenter().processIntent(getIntent());

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { //Update wallet balance change listener
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    if (fragments != null) {
                        for (Fragment fr : fragments) {
                            if (fr != null && fr.getClass() != null && fr instanceof WalletFragment){
                                ((WalletFragment)fr).initBalanceListener();
                            }
                        }
                    }
                }
            }
        });
    }

    private void initBottomNavViewWithFont(int fontId){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                TextView mSmallLabel = (TextView) item.findViewById(android.support.design.R.id.smallLabel);
                TextView mLargeLabel = (TextView) item.findViewById(android.support.design.R.id.largeLabel);
                mSmallLabel.setTypeface(FontManager.getInstance().getFont(getString(fontId)));
                mLargeLabel.setTypeface(FontManager.getInstance().getFont(getString(fontId)));
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mActivityResultListener!=null){
            mActivityResultListener.onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mPermissionsResultListener!=null) {
            mPermissionsResultListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        if(mAuthenticationListener!=null && !flag){
            mAuthenticationListener.onAuthenticate();
        }
    }

    @Override
    public MainActivity getActivity() {
            return this;
    }

    private int[] blackThemeIcons = {R.drawable.ic_wallet,R.drawable.ic_profile,R.drawable.ic_news,R.drawable.ic_send};
    private int[] lightThemeIcons = {R.drawable.ic_wallet_light,R.drawable.ic_profile_light,R.drawable.ic_news_light,R.drawable.ic_send_light};

    @Override
    protected void updateTheme() {

        setRootFragment(ProfileFragment.newInstance(this));
        openRootFragment(getPresenter().mRootFragment);

        if(ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)){
            mBottomNavigationView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background));
            mBottomNavigationView.setItemBackgroundResource(R.drawable.bottom_nav_view_tab_background);
            mBottomNavigationView.setItemTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
            mBottomNavigationView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
            resetNavBarIconsWithTheme(blackThemeIcons);
            recolorStatusBar(R.color.colorPrimary);
        } else {
            mBottomNavigationView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bottom_nav_bar_color_light));
            mBottomNavigationView.setItemBackgroundResource(android.R.color.transparent);
            mBottomNavigationView.setItemTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.bottom_nav_bar_text_color_light)));
            mBottomNavigationView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.bottom_nav_bar_text_color_light)));
            recolorStatusBar(R.color.title_color_light);
            resetNavBarIconsWithTheme(lightThemeIcons);
        }

        initBottomNavViewWithFont((ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)? R.string.simplonMonoRegular : R.string.proximaNovaRegular));

    }

    public void resetNavBarIconsWithTheme(int[] icons) {
        Menu menu = mBottomNavigationView.getMenu();
        menu.findItem(R.id.item_wallet).setIcon(icons[0]);
        menu.findItem(R.id.item_profile).setIcon(icons[1]);
        menu.findItem(R.id.item_news).setIcon(icons[2]);
        menu.findItem(R.id.item_send).setIcon(icons[3]);
    }

    public void recolorStatusBar(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), color));
        }
    }

    public void stopUpdateService(){
        getPresenter().stopUpdateService();
    }

    public void onLogin(){
        getPresenter().onLogin();
    }

    public void onLogout(){
        getPresenter().onLogout();
    }

    AuthenticationListener mAuthenticationListener;

    public void addAuthenticationListener(AuthenticationListener authenticationListener){
        mAuthenticationListener = authenticationListener;
    }

    public void removeAuthenticationListener(){
        mAuthenticationListener = null;
    }

    public interface AuthenticationListener{
        void onAuthenticate();
    }

    public void resetAuthFlags(){
        getPresenter().resetAuthFlags();
    }

}