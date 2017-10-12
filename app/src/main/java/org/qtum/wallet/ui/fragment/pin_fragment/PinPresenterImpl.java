package org.qtum.wallet.ui.fragment.pin_fragment;

import org.bitcoinj.wallet.Wallet;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.CryptoUtils;
import org.qtum.wallet.utils.CryptoUtilsCompat;
import org.qtum.wallet.utils.FingerprintUtils;
import org.qtum.wallet.utils.crypto.AESUtil;

import javax.crypto.Cipher;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PinPresenterImpl extends BaseFragmentPresenterImpl implements PinPresenter {

    private PinView mPinFragmentView;
    private PinInteractor mPinFragmentInteractor;
    private String pinRepeat;
    private String oldPin;
    private String pinHash;
    private String mAction;

    private boolean isDataLoaded = false;

    private int[] CREATING_STATE;
    private int[] AUTHENTICATION_STATE;
    private int[] CHANGING_STATE;

    private int currentState = 0;
    private boolean mTouchIdFlag;

    public PinPresenterImpl(PinView pinFragmentView, PinInteractor pinInteractor) {
        mPinFragmentView = pinFragmentView;
        int ENTER_PIN = R.string.enter_pin_lower_case;
        int ENTER_NEW_PIN = R.string.enter_new_pin;
        int REPEAT_PIN = R.string.repeat_pin;
        int ENTER_OLD_PIN = R.string.enter_old_pin;
        CREATING_STATE = new int[]{ENTER_NEW_PIN, REPEAT_PIN};
        AUTHENTICATION_STATE = new int[]{ENTER_PIN};
        CHANGING_STATE = new int[]{ENTER_OLD_PIN, ENTER_NEW_PIN, REPEAT_PIN};

        mPinFragmentInteractor = pinInteractor;
    }

    @Override
    public void confirm(final String pin) {
        switch (mAction) {
            case PinFragment.CREATING: {
                switch (currentState) {
                    case 0:
                        pinRepeat = pin;
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (pin.equals(pinRepeat)) {
                            getView().clearError();
                            getView().setProgressDialog();
                            getView().hideKeyBoard();
                            getInteractor().createWallet()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<String>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(String passphrase) {
                                            getInteractor().setKeyGeneratedInstance(true);
                                            isDataLoaded = true;

                                            byte[] saltPassphrase = AESUtil.encryptToBytes(pinRepeat, passphrase);
                                            getInteractor().saveSaltPassphrase(saltPassphrase);

                                            pinHash = CryptoUtilsCompat.generateSHA256String(pinRepeat);
                                            if (getView().checkAvailabilityTouchId()) {
                                                CryptoUtils.encodeInBackground(pinRepeat)
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(new Subscriber<String>() {
                                                            @Override
                                                            public void onCompleted() {

                                                            }

                                                            @Override
                                                            public void onError(Throwable e) {

                                                            }

                                                            @Override
                                                            public void onNext(String s) {

                                                                getInteractor().saveTouchIdPassword(s);
                                                                getInteractor().savePassword(pinHash);
                                                                getView().onLogin();
                                                                getView().openTouchIDPreferenceFragment(false,pinRepeat);
                                                                getView().dismissProgressDialog();
                                                                isDataLoaded = false;
                                                            }
                                                        });
                                            } else {
                                                getInteractor().savePassword(pinHash);
                                                getView().onLogin();
                                                getView().dismiss();
                                                getView().openBackUpWalletFragment(true,pinRepeat);
                                                getView().dismissProgressDialog();
                                                isDataLoaded = false;
                                            }
                                        }
                                    });

                        } else {
                            getView().confirmError(R.string.incorrect_repeated_pin);
                        }
                        break;
                }
            }
            break;

            case PinFragment.IMPORTING: {
                switch (currentState) {
                    case 0:
                        pinRepeat = pin;
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (pin.equals(pinRepeat)) {
                            getView().clearError();
                            getView().setProgressDialog();

                            byte[] saltPassphrase = AESUtil.encryptToBytes(pinRepeat, getView().getPassphrase());
                            getInteractor().saveSaltPassphrase(saltPassphrase);

                            pinHash = CryptoUtilsCompat.generateSHA256String(pinRepeat);

                            if (getView().checkAvailabilityTouchId()) {
                                CryptoUtils.encodeInBackground(pinRepeat)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<String>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(String s) {
                                                getInteractor().savePassword(pinHash);
                                                getInteractor().saveTouchIdPassword(s);
                                                getInteractor().setKeyGeneratedInstance(true);
                                                getView().dismissProgressDialog();
                                                getView().onLogin();
                                                getView().openTouchIDPreferenceFragment(true, pinRepeat);
                                            }
                                        });
                            } else {
                                getInteractor().savePassword(pinHash);
                                getInteractor().setKeyGeneratedInstance(true);
                                getView().onLogin();
                                getView().dismissProgressDialog();
                                getView().openWalletMainFragment();
                            }
                        } else {
                            getView().confirmError(R.string.incorrect_repeated_pin);
                        }
                        break;
                }
            }
            break;

            case PinFragment.AUTHENTICATION: {
                try {
                    String pinHashEntered = CryptoUtilsCompat.generateSHA256String(pin);
                    String pinHashGenuine = getInteractor().getPassword();
                    if (pinHashEntered.equals(pinHashGenuine)) {
                        getView().clearError();
                        getView().setProgressDialog();
                        getView().hideKeyBoard();
                        getInteractor().loadWalletFromFile()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Wallet>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Wallet wallet) {
                                        isDataLoaded = true;
                                        getView().onLogin();
                                        getView().dismissProgressDialog();
                                        getView().openWalletMainFragment();
                                    }
                                });
                    } else {
                        getView().confirmError(R.string.incorrect_pin);
                    }
                } catch (Exception e) {

                }
            }
            break;

            case PinFragment.CHECK_AUTHENTICATION: {
                String pinHashEntered = CryptoUtilsCompat.generateSHA256String(pin);
                String pinHashGenuine = getInteractor().getPassword();
                if (pinHashEntered.equals(pinHashGenuine)) {
                    getView().clearError();
                    getView().hideKeyBoard();
                    getView().setCheckAuthenticationShowFlag(false);
                    getView().onBackPressed();
                } else {
                    getView().confirmError(R.string.incorrect_pin);
                }
            }
            break;

            case PinFragment.AUTHENTICATION_FOR_PASSPHRASE: {
                String pinHashEntered = CryptoUtilsCompat.generateSHA256String(pin);
                String pinHashGenuine = getInteractor().getPassword();
                if (pinHashEntered.equals(pinHashGenuine)) {
                    getView().clearError();
                    getView().hideKeyBoard();
                    getView().setCheckAuthenticationShowFlag(false);
                    getView().openBackUpWalletFragment(false, pin);
                } else {
                    getView().confirmError(R.string.incorrect_pin);
                }
            }
            break;

            case PinFragment.AUTHENTICATION_AND_SEND: {
                String pinHashEntered = CryptoUtilsCompat.generateSHA256String(pin);
                String pinHashGenuine = getInteractor().getPassword();
                if (pinHashEntered.equals(pinHashGenuine)) {
                    getView().clearError();
                    final String address = getView().getAddressForSendAction();
                    final String amount = getView().getAmountForSendAction();
                    final String token = getView().getTokenForSendAction();
                    getView().setProgressDialog();
                    getView().hideKeyBoard();
                    getInteractor().loadWalletFromFile()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Wallet>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Wallet wallet) {
                                    isDataLoaded = true;
                                    getView().onLogin();
                                    getView().dismissProgressDialog();
                                    getView().openSendFragment(false, address, amount, token);
                                }
                            });
                } else {
                    getView().confirmError(R.string.incorrect_pin);
                }
            }
            break;

            case PinFragment.CHANGING: {
                switch (currentState) {
                    case 0:
                        oldPin = pin;
                        String pinHashEntered = CryptoUtilsCompat.generateSHA256String(pin);
                        String pinHashGenuine = getInteractor().getPassword();
                        if (pinHashEntered.equals(pinHashGenuine)) {
                            currentState = 1;
                            getView().clearError();
                            updateState();
                        } else {
                            getView().confirmError(R.string.incorrect_pin);
                        }
                        break;
                    case 1:
                        pinRepeat = pin;
                        currentState = 2;
                        getView().clearError();
                        updateState();
                        break;
                    case 2:
                        if (pin.equals(pinRepeat)) {
                            getView().clearError();

                            final String pinHash = CryptoUtilsCompat.generateSHA256String(pinRepeat);
                            getInteractor().savePassword(pinHash);
                            byte[] oldSaltPassphrase = getInteractor().getSaltPassphrase();
                            String passphrase = AESUtil.decryptBytes(oldPin, oldSaltPassphrase);
                            byte[] saltPassphrase = AESUtil.encryptToBytes(pinRepeat, passphrase);
                            getInteractor().saveSaltPassphrase(saltPassphrase);

                            if (getView().checkAvailabilityTouchId()) {
                                CryptoUtils.encodeInBackground(pinRepeat)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<String>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(String s) {
                                                getInteractor().saveTouchIdPassword(s);
                                                getView().onBackPressed();
                                            }
                                        });
                            } else {
                                getView().onBackPressed();
                            }
                        } else {
                            getView().confirmError(R.string.incorrect_repeated_pin);
                        }
                        break;
                }
            }
            break;
        }
    }

    @Override
    public void cancel() {
        switch (mAction) {

            case PinFragment.AUTHENTICATION:
            case PinFragment.AUTHENTICATION_AND_SEND:
            case PinFragment.CREATING:
            case PinFragment.IMPORTING:
            case PinFragment.AUTHENTICATION_FOR_PASSPHRASE:
                getView().onCancelClick();
                break;
            case PinFragment.CHECK_AUTHENTICATION:
            case PinFragment.CHANGING: {
                getView().onBackPressed();
                break;
            }
        }
    }

    @Override
    public void setAction(String action) {
        mAction = action;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTouchIdFlag = getView().checkTouchIdEnable();
        int titleID = 0;
        switch (mAction) {
            case PinFragment.IMPORTING:
            case PinFragment.CREATING:
                titleID = R.string.create_pin;
                break;
            case PinFragment.AUTHENTICATION_AND_SEND:
            case PinFragment.AUTHENTICATION:
            case PinFragment.CHECK_AUTHENTICATION:
            case PinFragment.AUTHENTICATION_FOR_PASSPHRASE:
                if (mTouchIdFlag && getView().isSensorStateAt(FingerprintUtils.mSensorState.READY)) {
                    titleID = R.string.confirm_fingerprint_or_pin;
                } else {
                    titleID = R.string.enter_pin;
                }
                break;
            case PinFragment.CHANGING:
                titleID = R.string.change_pin;
                break;
        }
        getView().setToolBarTitle(titleID);
    }

    @Override
    public void onPause() {
        super.onPause();
        pinRepeat = "0";
        currentState = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateState();
        if (isDataLoaded) {
            switch (mAction) {
                case PinFragment.CREATING: {
                    getInteractor().savePassword(pinHash);
                    getView().dismissProgressDialog();
                    getView().openBackUpWalletFragment(true, pinRepeat);
                    break;
                }
                case PinFragment.AUTHENTICATION: {
                    getView().openWalletMainFragment();
                    getView().dismissProgressDialog();
                    break;
                }
                case PinFragment.AUTHENTICATION_AND_SEND: {
                    final String address = getView().getAddressForSendAction();
                    final String amount = getView().getAmountForSendAction();
                    final String token = getView().getTokenForSendAction();
                    getView().openSendFragment(false, address, amount, token);
                    getView().dismissProgressDialog();
                    break;
                }
            }
            isDataLoaded = false;
        }

        if (mTouchIdFlag && (mAction.equals(PinFragment.AUTHENTICATION_AND_SEND) || mAction.equals(PinFragment.AUTHENTICATION) || mAction.equals(PinFragment.CHECK_AUTHENTICATION)) || mAction.equals(PinFragment.AUTHENTICATION_FOR_PASSPHRASE)) {
            getView().prepareSensor();
        }

    }

    @Override
    public PinView getView() {
        return mPinFragmentView;
    }

    private PinInteractor getInteractor() {
        return mPinFragmentInteractor;
    }

    private void updateState() {
        int state = 0;
        switch (mAction) {
            case PinFragment.IMPORTING:
            case PinFragment.CREATING:
                state = CREATING_STATE[currentState];
                break;
            case PinFragment.AUTHENTICATION:
            case PinFragment.AUTHENTICATION_AND_SEND:
            case PinFragment.CHECK_AUTHENTICATION:
            case PinFragment.AUTHENTICATION_FOR_PASSPHRASE:
                state = AUTHENTICATION_STATE[currentState];
                break;
            case PinFragment.CHANGING:
                state = CHANGING_STATE[currentState];
                break;
        }
        getView().updateState(state);
    }

    @Override
    public void onAuthenticationSucceeded(Cipher cipher) {
        String encoded = getInteractor().getTouchIdPassword();
        String decoded = CryptoUtils.decode(encoded, cipher);
        getView().setPin(decoded);
    }

}