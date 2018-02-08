package org.qtum.wallet.ui.activity.main_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;

import android.content.res.Configuration;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.qtum.wallet.QtumApplication;
import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;

import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.activity.splash_activity.SplashActivity;
import org.qtum.wallet.ui.base.base_activity.BaseActivity;
import org.qtum.wallet.ui.base.base_nav_fragment.BaseNavFragment;
import org.qtum.wallet.ui.fragment.news_fragment.NewsFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinAction;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;
import org.qtum.wallet.ui.fragment.qr_code_recognition_fragment.QrCodeRecognitionFragment;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletFragment;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;
import org.qtum.wallet.utils.CustomContextWrapper;
import org.qtum.wallet.utils.FontManager;

import org.qtum.wallet.ui.fragment.profile_fragment.ProfileFragment;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.utils.QtumIntent;
import org.qtum.wallet.utils.ResizeHeightAnimation;
import org.qtum.wallet.utils.ThemeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;

import static org.qtum.wallet.ui.base.base_fragment.BaseFragment.BACK_STACK_ROOT_TAG;

public class MainActivity extends BaseActivity implements MainActivityView {
    private static final int LAYOUT = R.layout.activity_main;
    private static final int LAYOUT_LIGHT = R.layout.activity_main_light;
    private MainActivityPresenter mMainActivityPresenterImpl;
    private ActivityResultListener mActivityResultListener;
    private PermissionsResultListener mPermissionsResultListener;
    private final int REQUEST_FINGERPRINT = 1000;
    protected Fragment mRootFragment;
    private Intent mIntent;
    private NetworkStateReceiver mNetworkReceiver;
    private UpdateService mUpdateService = null;
    private NetworkStateListener mNetworkStateListener;
    private String mAddressForSendAction;
    private String mAmountForSendAction;
    private String mTokenAddressForSendAction;
    private List<MainActivity.OnServiceConnectionChangeListener> mServiceConnectionChangeListeners = new ArrayList<>();
    private ServiceConnection mServiceConnection;

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    List<String> navigationBackStack = new ArrayList<>();

    FragmentKeyboardEventListener fragmentKeyboardEventListener;

    public void setFragmentKeyboardEventListener(FragmentKeyboardEventListener fragmentKeyboardEventListener) {
        this.fragmentKeyboardEventListener = fragmentKeyboardEventListener;
    }

    public void unsetFragmentKeyboardEventListener(){
        this.fragmentKeyboardEventListener = null;
    }

    boolean allowChangeKeyboardState = false;

    KeyboardVisibilityEventListener keyboardVisibilityEventListener = new KeyboardVisibilityEventListener() {
        @Override
        public void onVisibilityChanged(boolean isOpen) {

            if(allowChangeKeyboardState) {

                if (fragmentKeyboardEventListener != null) {
                    fragmentKeyboardEventListener.visibilityChange(isOpen);
                }

                if (isOpen) {
                    aCollapse(200);
                } else {
                    if (getPresenter().getAuthenticationFlag() && !getPresenter().isCheckAuthenticationShowFlag()) {
                        aExpand(200);
                    } else {
                        collapseBar();
                    }
                }
            }
        }
    };

    @Override
    protected void createPresenter() {
        mMainActivityPresenterImpl = new MainActivityPresenterImpl(this, new MainActivityInteractorImpl(getContext()));
    }

    @Override
    public MainActivityPresenter getPresenter() {
        return mMainActivityPresenterImpl;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            Log.d("BEST_TEST", savedInstanceState.toString());
        }
        setContentView((ThemeUtils.THEME_DARK.equals(ThemeUtils.currentTheme)) ? LAYOUT : LAYOUT_LIGHT);
        bindView();
        updateTheme();
        mNetworkReceiver = new NetworkStateReceiver(getNetworkConnectedFlag());
        registerReceiver(mNetworkReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder instanceof UpdateService.UpdateBinder) {
                    mUpdateService = ((UpdateService.UpdateBinder) iBinder).getService();
                    mUpdateService.clearNotification();
                    mUpdateService.startMonitoring();
                    for (MainActivity.OnServiceConnectionChangeListener listener : mServiceConnectionChangeListeners) {
                        listener.onServiceConnectionChange(true);
                    }
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };


        registerKeyboardListener();
    }

    private void registerKeyboardListener(){
        allowChangeKeyboardState = true;
       KeyboardVisibilityEvent.setEventListener(this, keyboardVisibilityEventListener);
    }

    public void unregisterKeyboardListener(){
        allowChangeKeyboardState = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case QtumIntent.OPEN_FROM_NOTIFICATION:
                    openRootWallet();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public String getAddressForSendAction() {
        return mAddressForSendAction;
    }

    public String getAmountForSendAction() {
        return mAmountForSendAction;
    }

    public String getTokenForSendAction() {
        return mTokenAddressForSendAction;
    }

    public void loadPermissions(String perm, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
    }

    public boolean checkPermission(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }


    public void openNavFragment(BaseNavFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment, fragment.getNavigationTag())
                .addToBackStack(fragment.getNavigationTag())
                .commit();
        navigationBackStack.add(fragment.getNavigationTag());
    }

    @Override
    public void openRootFragment(Fragment fragment) {
        getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void openFragment(Fragment fragment) {
        hideKeyBoard();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public UpdateService getUpdateService() {
        return mUpdateService;
    }

    public void subscribeServiceConnectionChangeEvent(OnServiceConnectionChangeListener listener) {
        mServiceConnectionChangeListeners.add(listener);
        listener.onServiceConnectionChange(mUpdateService != null);
    }

    public interface OnServiceConnectionChangeListener {
        void onServiceConnectionChange(boolean isConnecting);
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, 0);
    }

    @Override
    public void setIconChecked(int position) {
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void resetMenuText() {
        int[] menuResources = new int[]{R.string.wallet, R.string.profile, R.string.news, R.string.send};
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

    public boolean checkTouchIdEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (QtumSharedPreference.getInstance().isTouchIdEnable(getContext())) {
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                return checkPermission(Manifest.permission.USE_FINGERPRINT) && fingerprintManager.isHardwareDetected();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean checkAvailabilityTouchId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            return fingerprintManager != null && (checkPermission(Manifest.permission.USE_FINGERPRINT) && fingerprintManager.isHardwareDetected());
        } else {
            return false;
        }
    }

    private void aCollapse(int duration) {
        if(mBottomNavigationView.getHeight() > 0) {
            ResizeHeightAnimation resizeHeightAnimation = new ResizeHeightAnimation(mBottomNavigationView, pxFromDp(56), 0);
            resizeHeightAnimation.setDuration(duration);
            resizeHeightAnimation.setFillEnabled(true);
            resizeHeightAnimation.setFillAfter(true);
            mBottomNavigationView.startAnimation(resizeHeightAnimation);
        }
    }

    private void aExpand(int duration) {
        if(mBottomNavigationView.getHeight() == 0) {
            ResizeHeightAnimation resizeHeightAnimation = new ResizeHeightAnimation(mBottomNavigationView, 0, pxFromDp(56));
            resizeHeightAnimation.setDuration(duration);
            resizeHeightAnimation.setFillEnabled(true);
            resizeHeightAnimation.setFillAfter(true);
            mBottomNavigationView.startAnimation(resizeHeightAnimation);
        }
    }

    private void collapseBar() {
        if (mBottomNavigationView != null) {
            aCollapse(0);
        }
    }

    private void expandBar() {
        if (mBottomNavigationView != null && getPresenter().getAuthenticationFlag()) {
            aExpand(0);
        }
    }

    public int pxFromDp(final float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    public void showBottomNavigationView(boolean recolorStatusBar) {
        expandBar();
        if (recolorStatusBar) {
            recolorStatusBarBlue();
        }
    }

    public void hideBottomNavigationView(boolean recolorStatusBar) {
        collapseBar();
        if (recolorStatusBar) {
            recolorStatusBarBlack();
        }
    }

    public void recolorStatusBarBlack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.background));
        }
    }

    public void recolorStatusBarBlue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }
    }

    public void showBottomNavigationView(int resColorId) {
        expandBar();
        if (resColorId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), resColorId));
            }
        }
    }

    public void hideBottomNavigationView(int resColorId) {
        collapseBar();
        if (resColorId > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), resColorId));
            }
        }
    }

    @Override
    public void initializeViews() {

        initBottomNavViewWithFont((ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK) ? R.string.simplonMonoRegular : R.string.proximaNovaRegular));

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_wallet:
                        if (checkFragmentExistance(WalletMainFragment.class.getCanonicalName(), true)) {
                            return true;
                        }
                        mRootFragment = WalletMainFragment.newInstance(getContext());
                        break;
                    case R.id.item_profile:
                        if (checkFragmentExistance(ProfileFragment.class.getCanonicalName(), true)) {
                            return true;
                        }
                        mRootFragment = ProfileFragment.newInstance(getContext());
                        break;
                    case R.id.item_news:
                        if (checkFragmentExistance(NewsFragment.class.getCanonicalName(), true)) {
                            return true;
                        }
                        mRootFragment = NewsFragment.newInstance(getContext());
                        break;
                    case R.id.item_send:
                        if (checkFragmentExistance(SendFragment.class.getCanonicalName(), true)) {
                            return true;
                        }
                        mRootFragment = SendFragment.newInstance(false, null, null, null, getContext());
                        break;
                    default:
                        return false;
                }
                openNavFragment((BaseNavFragment) mRootFragment);
                return true;
            }
        });

        Intent intent = getIntent();
        switch (intent.getAction()) {
            case QtumIntent.SEND_FROM_SDK:
                getPresenter().setSendFromIntent(true);
                mAddressForSendAction = intent.getStringExtra(QtumIntent.SEND_ADDRESS);
                mAmountForSendAction = intent.getStringExtra(QtumIntent.SEND_AMOUNT);
                mTokenAddressForSendAction = intent.getStringExtra(QtumIntent.SEND_TOKEN);
                break;
            case NfcAdapter.ACTION_NDEF_DISCOVERED:
                getPresenter().setSendFromIntent(true);
                mAddressForSendAction = "QbShaLBf1nAX3kznmGU7vM85HFRYJVG6ut";
                mAmountForSendAction = "1.431";
                break;
            default:
                break;
        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { //Update wallet balance change listener
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    if (fragments != null) {
                        if (startPageExists(fragments)) {
                            return;
                        }
                        for (Fragment fr : fragments) {
                            if (fr != null && fr.getClass() != null) {
                                if (fr instanceof WalletFragment) {
                                    showBottomNavigationView(false);
                                } else if (fr instanceof NewsFragment) {
                                    showBottomNavigationView(false);
                                }
                            }
                        }
                    }
                }
            }
        });

        mNetworkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                getPresenter().updateNetworkSate(networkConnectedFlag);
            }
        };
        mNetworkReceiver.addNetworkStateListener(mNetworkStateListener);
    }

    private boolean checkFragmentExistance(String tag, boolean addToBackStack) {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragmentByTag != null && fragmentByTag instanceof BaseNavFragment) {

            if (addToBackStack && tag.equals(navigationBackStack.get(navigationBackStack.size() - 1))) {
                ((BaseNavFragment) fragmentByTag).clearChildBackStack();
                return true;
            }

            mRootFragment = fragmentByTag;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            for (Fragment fr : getSupportFragmentManager().getFragments()) {
                transaction.hide(fr);
            }
            transaction.show(mRootFragment).commit();
            if (addToBackStack) {
                if (navigationBackStack.contains(((BaseNavFragment) mRootFragment).getNavigationTag())) {
                    navigationBackStack.removeAll(Collections.singleton(((BaseNavFragment) mRootFragment).getNavigationTag()));
                    if (((BaseNavFragment) mRootFragment).getNavigationTag().equals(WalletMainFragment.class.getCanonicalName())) {
                        navigationBackStack.add(0, ((BaseNavFragment) mRootFragment).getNavigationTag());
                    }
                }
                navigationBackStack.add(((BaseNavFragment) mRootFragment).getNavigationTag());
            }

            return true;
        }
        return false;
    }

    boolean startPageExists(List<Fragment> fragments) {
        for (Fragment fr : fragments) {
            if (fr instanceof StartPageFragment) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentNavFragment(String tag){
        return navigationBackStack.get(navigationBackStack.size() -1).equals(tag);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressLint("RestrictedApi")
    private void initBottomNavViewWithFont(int fontId) {
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
        if (mActivityResultListener != null) {
            getPresenter().setCheckAuthenticationFlag(false);
            mActivityResultListener.onActivityResult(requestCode, resultCode, data);
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
        if (mPermissionsResultListener != null) {
            mPermissionsResultListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNetworkReceiver.removeNetworkStateListener(mNetworkStateListener);
    }

    public void setRootFragment(Fragment rootFragment) {
        mRootFragment = rootFragment;
    }

    public NetworkStateReceiver getNetworkReceiver() {
        return mNetworkReceiver;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CustomContextWrapper.wrap(newBase, QtumSettingSharedPreference.getInstance().getLanguage(newBase)));
    }

    public QtumApplication getQtumApplication() {
        return (QtumApplication) getApplication();
    }

    public void addActivityResultListener(ActivityResultListener activityResultListener) {
        mActivityResultListener = activityResultListener;
    }

    public void removeResultListener() {
        mActivityResultListener = null;
    }

    public void addPermissionResultListener(PermissionsResultListener permissionsResultListener) {
        mPermissionsResultListener = permissionsResultListener;
    }

    public void removePermissionResultListener() {
        mPermissionsResultListener = null;
    }

    public interface ActivityResultListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public interface PermissionsResultListener {
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    @Override
    public void onBackPressed() {

        if (getPresenter().shouldShowPin()) {
            clearBackStack();
            openRootFragment(StartPageFragment.newInstance(this));
            return;
        }

        if (getPresenter().getAuthenticationFlag()) {
            if (navigationBackStack.size() > 0) {
                String tag = navigationBackStack.get(navigationBackStack.size() - 1);
                if (!onChildBackPressed(tag)) {
                    if (navigationBackStack.size() == 1) {
                        ActivityCompat.finishAffinity(this);
                    } else {
                        navigationBackStack.remove(navigationBackStack.size() - 1);
                        checkFragmentExistance(navigationBackStack.get(navigationBackStack.size() - 1), false);
                    }
                }
            } else {
                ActivityCompat.finishAffinity(this);
            }
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                super.onBackPressed();
            } else {
                ActivityCompat.finishAffinity(this);
            }
        }
    }

    private boolean onChildBackPressed(String tag) {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragmentByTag == null || !(fragmentByTag instanceof BaseNavFragment)) {
            return false;
        }
        return ((BaseNavFragment) fragmentByTag).onBackPressed();
    }

    public boolean checkNavFragmentExistance(String tag) {
        return navigationBackStack.contains(tag);
    }

    public boolean checkSendQrCodeActive() {
        if (!checkNavFragmentExistance(SendFragment.class.getCanonicalName())) {
            return false;
        } else {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(SendFragment.class.getCanonicalName());
            if (fragmentByTag != null) {
                if (fragmentByTag.getChildFragmentManager().findFragmentByTag(QrCodeRecognitionFragment.class.getCanonicalName()) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void moveToQrCodeActiveSend() {
        checkFragmentExistance(SendFragment.class.getCanonicalName(), true);
    }

    public void setCheckAuthenticationShowFlag(boolean flag) {
        getPresenter().setCheckAuthenticationShowFlag(flag);
        if (mAuthenticationListener != null && !flag) {
            mAuthenticationListener.onAuthenticate();
        }
    }

    @Override
    public MainActivity getActivity() {
        return this;
    }

    private int[] blackThemeIcons = {R.drawable.ic_wallet, R.drawable.ic_profile, R.drawable.ic_news, R.drawable.ic_send};
    private int[] lightThemeIcons = {R.drawable.ic_wallet_light, R.drawable.ic_profile_light, R.drawable.ic_news_light, R.drawable.ic_send_light};

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentManager.BackStackEntry backStackEntryAt = fm.getBackStackEntryAt(0);
        fm.popBackStack(backStackEntryAt.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        navigationBackStack.clear();
    }

    @Override
    protected void updateTheme() {

        if (mRootFragment != null && mRootFragment instanceof ProfileFragment) {

            clearBackStack();
            openNavFragment(WalletMainFragment.newInstance(this));
            mRootFragment = ProfileFragment.newInstance(this);
            openNavFragment((BaseNavFragment) mRootFragment);
        }

        if (ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)) {
            mBottomNavigationView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background));
            mBottomNavigationView.setItemBackgroundResource(R.drawable.bottom_nav_view_tab_background);
            mBottomNavigationView.setItemTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
            mBottomNavigationView.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
            resetNavBarIconsWithTheme(blackThemeIcons);
            recolorStatusBar(R.color.colorPrimary);
        } else {
            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };

            int[] colors = new int[]{
                    ContextCompat.getColor(getContext(), R.color.bottom_nav_bar_text_color_light),
                    ContextCompat.getColor(getContext(), R.color.bottom_nav_bar_text_color_light_alpha_50)
            };
            ColorStateList myList = new ColorStateList(states, colors);

            mBottomNavigationView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bottom_nav_bar_color_light));
            mBottomNavigationView.setItemBackgroundResource(android.R.color.transparent);
            mBottomNavigationView.setItemTextColor(myList);
            mBottomNavigationView.setItemIconTintList(myList);
            recolorStatusBar(R.color.title_color_light);
            resetNavBarIconsWithTheme(lightThemeIcons);
        }
        initBottomNavViewWithFont((ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK) ? R.string.simplonMonoRegular : R.string.proximaNovaRegular));
        registerKeyboardListener();
    }

    public void resetNavBarIconsWithTheme(int[] icons) {
        Menu menu = mBottomNavigationView.getMenu();
        menu.findItem(R.id.item_wallet).setIcon(icons[0]);
        menu.findItem(R.id.item_profile).setIcon(icons[1]);
        menu.findItem(R.id.item_news).setIcon(icons[2]);
        menu.findItem(R.id.item_send).setIcon(icons[3]);
    }

    public void recolorStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), color));
        }
    }

    public void recolorStatusBar(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
    }

    public void stopUpdateService() {
        if (mUpdateService != null) {
            mUpdateService.stopMonitoring();
            stopService(mIntent);
        }
    }

    public void onLogin() {
        getPresenter().onLogin();
        mIntent = new Intent(getContext(), UpdateService.class);
        if (!isMyServiceRunning(UpdateService.class)) {
            startService(mIntent);
            if (mUpdateService != null) {
                mUpdateService.startMonitoring();
            } else {
                bindService(mIntent, mServiceConnection, 0);
            }
        } else {
            if (mUpdateService != null) {
                mUpdateService.startMonitoring();
            } else {
                bindService(mIntent, mServiceConnection, 0);
            }
        }
        openRootWallet();
        registerKeyboardListener();
    }

    public void onAuth(){
        registerKeyboardListener();
        showBottomNavigationView(ThemeUtils.currentTheme.equals(ThemeUtils.THEME_DARK)? R.color.background : R.color.title_color_light);
    }

    public void openRootWallet() {
        clearBackStack();
        hideKeyBoard();
        openNavFragment(WalletMainFragment.newInstance(this));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onLogout() {
        clearBackStack();
        if (mUpdateService != null) {
            mUpdateService.stopMonitoring();
        }
        getPresenter().onLogout();
    }

    AuthenticationListener mAuthenticationListener;

    public void addAuthenticationListener(AuthenticationListener authenticationListener) {
        mAuthenticationListener = authenticationListener;
    }

    public void removeAuthenticationListener() {
        mAuthenticationListener = null;
    }

    public interface AuthenticationListener {
        void onAuthenticate();
    }

    public void resetAuthFlags() {
        getPresenter().resetAuthFlags();
    }

    @Override
    public void openPinFragmentRoot(PinAction action) {
        Fragment fragment = PinFragment.newInstance(action, getContext());
        openRootFragment(fragment);
    }

    @Override
    public void openPinFragment(PinAction action) {
        Fragment fragment = PinFragment.newInstance(action, getContext());
        openFragment(fragment);
    }

    @Override
    public void openStartPageFragment(boolean isLogin) {
        Fragment fragment = StartPageFragment.newInstance(getContext());
        openRootFragment(fragment);
        if (isLogin) {
            fragment = PinFragment.newInstance(PinAction.AUTHENTICATION, getContext());
            openRootFragment(fragment);
        }
    }

    @Override
    public void clearService() {
        if (mUpdateService != null) {
            unbindService(mServiceConnection);
            unregisterReceiver(mNetworkReceiver);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void checkSensorState(final SensorStateListener sensorStateListener) {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isKeyguardSecure()) {
            sensorStateListener.onRequest(SensorState.NOT_BLOCKED);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!FingerprintManagerCompat.from(this).hasEnrolledFingerprints()) {
                sensorStateListener.onRequest(SensorState.NO_FINGERPRINTS);
            } else {
                sensorStateListener.onRequest(SensorState.READY);
            }
        } else {
            final FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    sensorStateListener.onRequest(SensorState.NO_FINGERPRINTS);
                } else {
                    sensorStateListener.onRequest(SensorState.READY);
                }
            } else {
                loadPermissions(Manifest.permission.USE_FINGERPRINT, REQUEST_FINGERPRINT);
                addPermissionResultListener(new PermissionsResultListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                        if (requestCode == REQUEST_FINGERPRINT) {
                            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                                if (!fingerprintManager.hasEnrolledFingerprints()) {
                                    sensorStateListener.onRequest(SensorState.NO_FINGERPRINTS);
                                } else {
                                    sensorStateListener.onRequest(SensorState.READY);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public interface SensorStateListener {
        void onRequest(SensorState sensorState);
    }

    public enum SensorState {
        NOT_BLOCKED,
        NO_FINGERPRINTS,
        READY
    }

    public Realm getRealm(){
        return ((QtumApplication)getApplication()).getRealm();
    }
}