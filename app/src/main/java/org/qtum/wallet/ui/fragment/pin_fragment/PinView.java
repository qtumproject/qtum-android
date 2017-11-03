package org.qtum.wallet.ui.fragment.pin_fragment;

import android.support.annotation.StringRes;

import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


public interface PinView extends BaseFragmentView {
    void confirmError(String errorText);
    void confirmError(@StringRes int resId);
    void updateState(int state);
    void clearError();
    void setToolBarTitle(int titleID);
    void setPin(String pin);
    void prepareSensor();
    boolean checkTouchIdEnable();
    String getPassphrase();
    void checkSensorState(MainActivity.SensorStateListener sensorStateListener);
    void openTouchIDPreferenceFragment(boolean isImporting, String pin);
    void openWalletMainFragment();
    void openBackUpWalletFragment(boolean isWalletCreating, String pin);
    void openSendFragment(boolean qrCodeRecognition, String address, String amount, String tokenAddress);
    boolean checkAvailabilityTouchId();
    String getAddressForSendAction();
    String getAmountForSendAction();
    String getTokenForSendAction();
    void setDigitPin(int digit);
    void onLogin();
    void onCancelClick();
    void onBackPressed();
    void setCheckAuthenticationShowFlag(boolean checkAuthenticationShowFlag);
    void clearPin();
    void saveCode(String code);
}
