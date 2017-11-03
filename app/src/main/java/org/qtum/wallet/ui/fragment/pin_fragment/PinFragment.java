package org.qtum.wallet.ui.fragment.pin_fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment.touch_id_preference_fragment.TouchIDPreferenceFragment;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.CryptoUtils;

import org.qtum.wallet.utils.PinEntryEditText;
import org.qtum.wallet.utils.FontTextView;

import javax.crypto.Cipher;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class PinFragment extends BaseFragment implements PinView {

    private PinPresenter mPinFragmentPresenter;

    private final static String ACTION = "action";
    private final static String PASSPHRASE = "passphrase";

    private FingerprintHelper mFingerprintHelper;
    private FingerPrintHelperCompat23 mFingerPrintHelperCompat23;

    @BindView(R.id.til_wallet_pin)
    PinEntryEditText mWalletPin;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    FontTextView mTextViewToolBarTitle;

    @BindView(R.id.tooltip)
    FontTextView tooltip;

    @OnClick({R.id.bt_cancel})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                getPresenter().cancel();
                break;
        }
    }

    @Override
    public void onCancelClick() {
        getMainActivity().resetAuthFlags();
        openRootFragment(StartPageFragment.newInstance(false, getContext()));
    }

    @Override
    public void setCheckAuthenticationShowFlag(boolean checkAuthenticationShowFlag) {
        getMainActivity().setCheckAuthenticationShowFlag(checkAuthenticationShowFlag);
    }

    @Override
    public void onBackPressed(){
        getMainActivity().onBackPressed();
    }

    public static BaseFragment newInstance(PinAction action, String passphrase, Context context) {
        BaseFragment pinFragment = Factory.instantiateFragment(context, PinFragment.class);
        Bundle args = new Bundle();
        args.putSerializable(ACTION, action);
        args.putString(PASSPHRASE, passphrase);
        pinFragment.setArguments(args);
        return pinFragment;
    }

    public static BaseFragment newInstance(PinAction action, Context context) {
        BaseFragment pinFragment = Factory.instantiateFragment(context, PinFragment.class);
        Bundle args = new Bundle();
        args.putSerializable(ACTION, action);
        pinFragment.setArguments(args);
        return pinFragment;
    }

    @Override
    protected void createPresenter() {
        mPinFragmentPresenter = new PinPresenterImpl(this, new PinInteractorImpl(getContext()));
    }

    @Override
    protected PinPresenter getPresenter() {
        return mPinFragmentPresenter;
    }

    @Override
    public void confirmError(String errorText) {
        if (mWalletPin != null) {
            mWalletPin.setText("");
            tooltip.setText(errorText);
            tooltip.setTextColor(ContextCompat.getColor(getContext(), R.color.accent_red_color));
            tooltip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void confirmError(@StringRes int resId) {
        if (mWalletPin != null) {
            mWalletPin.setText("");
            tooltip.setText(resId);
            tooltip.setTextColor(ContextCompat.getColor(getContext(), R.color.accent_red_color));
            tooltip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clearPin() {
        mWalletPin.setText("");
    }

    @Override
    public void updateState(int state) {
        mWalletPin.setText("");
        tooltip.setText(state);
        tooltip.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        tooltip.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean checkTouchIdEnable() {
        return getMainActivity().checkTouchIdEnable();
    }

    @Override
    public boolean checkAvailabilityTouchId() {
        return getMainActivity().checkAvailabilityTouchId();
    }

    @Override
    public void clearError() {
        tooltip.setText("");
        tooltip.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setToolBarTitle(int titleID) {
        mTextViewToolBarTitle.setText(titleID);
    }

    @Override
    public void setPin(String pin) {
        mWalletPin.setText(pin);
    }

    @Override
    public String getPassphrase() {
        return getArguments().getString(PASSPHRASE);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hideKeyBoard();
    }

    Handler softHandler;

    @Override
    public void onResume() {
        super.onResume();
        setSoftMode();
        mWalletPin.setFocusableInTouchMode(true);
        mWalletPin.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mWalletPin, InputMethodManager.SHOW_IMPLICIT);

    }

    @Override
    public void initializeViews() {
        softHandler = new Handler();
        getPresenter().setAction((PinAction) getArguments().getSerializable(ACTION));
        mWalletPin.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                getPresenter().confirm(str.toString());
            }
        });
    }

    @Override
    public void prepareSensor() {

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    FingerprintManagerCompat.CryptoObject cryptoObject = CryptoUtils.getCryptoObject();
                    if (cryptoObject != null) {
                        mFingerprintHelper = new FingerprintHelper(getContext());
                        mFingerprintHelper.startAuth(cryptoObject);
                    } else {
                        //TODO: make
                        Toast.makeText(getContext(), "new fingerprint enrolled. enter pin again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    FingerprintManager.CryptoObject cryptoObject = CryptoUtils.getCryptoObjectCompat23();
                    if (cryptoObject != null) {
                        mFingerPrintHelperCompat23 = new FingerPrintHelperCompat23(getContext());
                        mFingerPrintHelperCompat23.startAuth(cryptoObject);
                    } else {
                        //TODO: make
                        Toast.makeText(getContext(), "new fingerprint enrolled. enter pin again", Toast.LENGTH_SHORT).show();
                    }
                }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public class FingerPrintHelperCompat23 extends FingerprintManager.AuthenticationCallback{

        private Context mContext;
        private android.os.CancellationSignal mCancellationSignal;

        FingerPrintHelperCompat23(Context context) {
            mContext = context;
        }

        void cancel() {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }

        void startAuth(FingerprintManager.CryptoObject cryptoObject) {
            mCancellationSignal = new android.os.CancellationSignal();
            FingerprintManager manager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);
            manager.authenticate(cryptoObject, mCancellationSignal, 0,  this, null);
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            confirmError(errString.toString());
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            confirmError("try again");
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            confirmError(helpString.toString());
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            Cipher cipher = result.getCryptoObject().getCipher();
            getPresenter().onAuthenticationSucceeded(cipher);

        }
    }

    public class FingerprintHelper extends FingerprintManagerCompat.AuthenticationCallback {
        private Context mContext;
        private CancellationSignal mCancellationSignal;

        FingerprintHelper(Context context) {
            mContext = context;
        }

        void startAuth(FingerprintManagerCompat.CryptoObject cryptoObject) {
            mCancellationSignal = new CancellationSignal();
            FingerprintManagerCompat manager = FingerprintManagerCompat.from(mContext);
            manager.authenticate(cryptoObject, 0, mCancellationSignal, this, null);
        }

        void cancel() {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            confirmError(errString.toString());
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            confirmError(helpString.toString());
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Cipher cipher = result.getCryptoObject().getCipher();
            getPresenter().onAuthenticationSucceeded(cipher);
        }

        @Override
        public void onAuthenticationFailed() {
            confirmError("try again");
        }
    }

    @Override
    public void checkSensorState(MainActivity.SensorStateListener sensorStateListener) {
        getMainActivity().checkSensorState(sensorStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mFingerprintHelper != null) {
                mFingerprintHelper.cancel();
            }
        } else {
            if (mFingerPrintHelperCompat23 != null) {
                mFingerPrintHelperCompat23.cancel();
            }
        }
    }

    @Override
    public void openTouchIDPreferenceFragment(boolean isImporting, String pin ){
        Fragment fragment = TouchIDPreferenceFragment.newInstance(getContext(), isImporting, pin);
        openRootFragment(fragment);
    }

    @Override
    public void openWalletMainFragment() {
        Fragment fragment = WalletMainFragment.newInstance(getContext());
        getMainActivity().setRootFragment(fragment);
        openRootFragment(fragment);
    }

    @Override
    public void openSendFragment(boolean qrCodeRecognition, String address, String amount, String tokenAddress) {
        final BaseFragment sendFragment = SendFragment.newInstance(false, address, amount, tokenAddress, getContext());
        getMainActivity().setRootFragment(sendFragment);
        openRootFragment(sendFragment);
    }

    @Override
    public void openBackUpWalletFragment(boolean isWalletCreating, String pin) {
        if(isWalletCreating){
            Fragment backUpWalletFragment = BackUpWalletFragment.newInstance(getContext(), isWalletCreating, pin);
            openFragmentWithBackStack(backUpWalletFragment, backUpWalletFragment.getClass().getName());
        } else {
            Fragment backUpWalletFragment = BackUpWalletFragment.newInstance(getContext(), isWalletCreating, pin);
            openFragment(backUpWalletFragment);
        }
    }

    @Override
    public String getAddressForSendAction() {
        return getMainActivity().getAddressForSendAction();
    }

    @Override
    public String getAmountForSendAction() {
        return getMainActivity().getAmountForSendAction();
    }

    @Override
    public String getTokenForSendAction() {
        return getMainActivity().getTokenForSendAction();
    }

    @Override
    public void onLogin() {
        getMainActivity().onLogin();
    }

    @Override
    public void setDigitPin(int digit) {
        mWalletPin.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(digit)
        });
    }

    @Override
    public void saveCode(String code) {
        ((MainActivity)getActivity()).getPresenter().setCode(code);
    }
}
