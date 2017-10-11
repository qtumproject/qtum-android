package org.qtum.wallet.ui.fragment.pin_fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment.touch_id_preference_fragment.TouchIDPreferenceFragment;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.CryptoUtils;
import org.qtum.wallet.utils.FingerprintUtils;
import org.qtum.wallet.utils.FontTextView;

import javax.crypto.Cipher;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class PinFragment extends BaseFragment implements PinView {

    private PinPresenter mPinFragmentPresenter;

    private final static String ACTION = "action";
    private final static String PASSPHRASE = "passphrase";

    public final static String CREATING = "creating";
    public final static String CHECK_AUTHENTICATION = "check_authentication";
    public final static String AUTHENTICATION = "authentication";
    public final static String AUTHENTICATION_FOR_PASSPHRASE = "authentication_for_passphrase";
    public final static String AUTHENTICATION_AND_SEND = "authentication_and_send";
    public final static String CHANGING = "changing";
    public final static String IMPORTING = "importing";

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
                onCancelClick();
                break;
        }
    }

    void onCancelClick() {
        getMainActivity().resetAuthFlags();
        openRootFragment(StartPageFragment.newInstance(false, getContext()));
    }

    public static BaseFragment newInstance(String action, String passphrase, Context context) {
        BaseFragment pinFragment = Factory.instantiateFragment(context, PinFragment.class);
        Bundle args = new Bundle();
        args.putString(ACTION, action);
        args.putString(PASSPHRASE, passphrase);
        pinFragment.setArguments(args);
        return pinFragment;
    }

    public static BaseFragment newInstance(String action, Context context) {
        BaseFragment pinFragment = Factory.instantiateFragment(context, PinFragment.class);
        Bundle args = new Bundle();
        args.putString(ACTION, action);
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
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyBoard();
    }

    Handler softHandler;

    @Override
    public void onResume() {
        super.onResume();
        showSoftInput();
        mWalletPin.setFocusableInTouchMode(true);
        mWalletPin.requestFocus();
    }

    @Override
    public void initializeViews() {
        softHandler = new Handler();
        getPresenter().setAction(getArguments().getString(ACTION));
        mWalletPin.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str.length() == 4) {
                    getPresenter().confirm(str.toString());
                }
            }
        });
    }

    @Override
    public void prepareSensor() {
            if (FingerprintUtils.isSensorStateAt(FingerprintUtils.mSensorState.READY, getContext())) {
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
    public boolean isSensorStateAt(FingerprintUtils.mSensorState sensorState) {
        return FingerprintUtils.isSensorStateAt(sensorState, getContext());
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
    public void openBackUpWalletFragment(boolean isWalletCreating, String pin) {
        Fragment backUpWalletFragment = BackUpWalletFragment.newInstance(getContext(), false, pin);
        getMainActivity().onBackPressed();
        openFragment(backUpWalletFragment);
    }
}
