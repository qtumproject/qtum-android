package org.qtum.wallet.ui.fragment.pin_fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.utils.CryptoUtilsCompat;
import org.qtum.wallet.utils.PinEntryEditText;
import org.qtum.wallet.utils.ThemeUtils;
import org.qtum.wallet.utils.crypto.KeyStoreHelper;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.utils.CryptoUtils;
import org.qtum.wallet.utils.FontTextView;

import java.util.Calendar;

import javax.crypto.Cipher;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PinDialogFragment extends DialogFragment {

    @BindView(org.qtum.wallet.R.id.til_wallet_pin)
    PinEntryEditText mWalletPin;

    @BindView(org.qtum.wallet.R.id.tv_toolbar_title)
    FontTextView mTextViewToolBarTitle;

    @BindView(org.qtum.wallet.R.id.tooltip)
    FontTextView tooltip;

    private final String QTUM_PIN_ALIAS = "qtum_alias";

    private PinCallBack mPinCallBack;
    private boolean mTouchIdFlag;
    private FingerprintHelper mFingerprintHelper;
    private final Long mBanTime = 600000L;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK) ? org.qtum.wallet.R.layout.dialog_fragment_pin : org.qtum.wallet.R.layout.dialog_fragment_pin_light, null);
        ButterKnife.bind(this, view);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if (getSixDigitPassword().isEmpty()) {
            mWalletPin.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(4)
            });
        } else {
            mWalletPin.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(6)
            });
        }
        return dialog;
    }

    private void hideKeyBoard() {
        Activity activity = getActivity();
        View view = activity.getCurrentFocus();
        if (view != null) {
            hideKeyBoard(activity, view);
        }
    }

    public void hideKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private String getSixDigitPassword() {
        String encryptedPinHash = QtumSharedPreference.getInstance().getSixDigitPassword(getContext());
        if (encryptedPinHash.isEmpty()) {
            return encryptedPinHash;
        }
        return KeyStoreHelper.decrypt(QTUM_PIN_ALIAS, encryptedPinHash);
    }

    private String getPassword() {
        String sixDigitPassword = getSixDigitPassword();
        if (!getSixDigitPassword().isEmpty()) {
            return sixDigitPassword;
        } else {
            return getFourDigitPassword();
        }
    }

    private String getFourDigitPassword() {
        String encryptedPinHash = QtumSharedPreference.getInstance().getPassword(getContext());
        return KeyStoreHelper.decrypt(QTUM_PIN_ALIAS, encryptedPinHash);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWalletPin.requestFocus();
        if (mTouchIdFlag) {
            ((MainActivity) getActivity()).checkSensorState(new MainActivity.SensorStateListener() {
                @Override
                public void onRequest(MainActivity.SensorState sensorState) {
                    if (sensorState == MainActivity.SensorState.READY) {
                        mTextViewToolBarTitle.setText(org.qtum.wallet.R.string.confirm_fingerprint_or_pin);
                        prepareSensor();
                    } else {
                        mTextViewToolBarTitle.setText(org.qtum.wallet.R.string.enter_pin);
                    }
                }
            });
        } else {
            mTextViewToolBarTitle.setText(org.qtum.wallet.R.string.enter_pin);
        }

        mWalletPin.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                confirm(str.toString());
            }
        });
        mWalletPin.setFocusableInTouchMode(true);
        mWalletPin.requestFocus();

        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void setPin(String pin) {
        mWalletPin.setText(pin);
    }

    private void clearError() {
        tooltip.setText("");
        tooltip.setVisibility(View.INVISIBLE);
    }

    private void confirmError(String errorText) {
        if (getDialog() != null) {
            mWalletPin.setText("");
            tooltip.setText(errorText);
            tooltip.setTextColor(ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.accent_red_color));
            tooltip.setVisibility(View.VISIBLE);
        }
    }

    private void prepareSensor() {
        FingerprintManagerCompat.CryptoObject cryptoObject = CryptoUtils.getCryptoObject();
        if (cryptoObject != null) {
            mFingerprintHelper = new FingerprintHelper(getContext());
            mFingerprintHelper.startAuth(cryptoObject);
        } else {
            Toast.makeText(getContext(), "new fingerprint enrolled. enter pin again", Toast.LENGTH_SHORT).show();
        }
    }

    private class FingerprintHelper extends FingerprintManagerCompat.AuthenticationCallback {
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
            String encoded = QtumSharedPreference.getInstance().getTouchIdPassword(getContext());
            String decoded = CryptoUtils.decode(encoded, cipher);
            setPin(decoded);
        }

        @Override
        public void onAuthenticationFailed() {
            confirmError("try again");
        }
    }

    private void confirm(String pin) {
        Long banTime = QtumSharedPreference.getInstance().getBanTime(getContext());
        Long currentTime = Calendar.getInstance().getTimeInMillis();
        Long remainingTimeOfBan = banTime - currentTime;
        if (remainingTimeOfBan > 0) {
            int min = (int) Math.ceil(remainingTimeOfBan / 60000.);
            hideKeyBoard();
            dismiss();
            mPinCallBack.onError(getString(R.string.sorry_please_try_again_in) + " " + min + " " + getString(R.string.minutes));
            return;
        }
        String pinHashEntered = CryptoUtilsCompat.generateSHA256String(pin);
        String pinHashGenuine = getPassword();
        boolean isCorrect = pinHashEntered.equals(pinHashGenuine);
        changeBanState(isCorrect);
        if (isCorrect) {
            clearError();
            mPinCallBack.onSuccess();
            dismiss();
        } else {
            confirmError(getContext().getString(org.qtum.wallet.R.string.incorrect_pin));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFingerprintHelper != null) {
            mFingerprintHelper.cancel();
        }
        dismiss();
    }

    private void changeBanState(boolean isCorrect) {
        int failedAttemptsCount;
        if (isCorrect) {
            failedAttemptsCount = 0;
        } else {
            failedAttemptsCount = QtumSharedPreference.getInstance().getFailedAttemptsCount(getContext());
            failedAttemptsCount++;
            if (failedAttemptsCount == 3) {
                Long currentTime = Calendar.getInstance().getTimeInMillis();
                Long banTime = currentTime + mBanTime;
                QtumSharedPreference.getInstance().setBanTime(getContext(), banTime);
                failedAttemptsCount = 0;
                mPinCallBack.onError(getString(R.string.sorry_please_try_again_in) + " " + mBanTime.intValue() / 60000 + " " + getString(R.string.minutes));
            }
        }
        QtumSharedPreference.getInstance().setFailedAttemptsCount(getContext(), failedAttemptsCount);
    }

    public void addPinCallBack(PinCallBack callBack) {
        mPinCallBack = callBack;
    }

    void removePinCallBack() {
        mPinCallBack = null;
    }

    public void setTouchIdFlag(boolean touchIdFlag) {
        mTouchIdFlag = touchIdFlag;
    }

    public interface PinCallBack {
        void onSuccess();

        void onError(String error);
    }
}
