package org.qtum.wallet.ui.fragment.pin_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.Calendar;

import javax.crypto.Cipher;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION_AND_SEND;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION_FOR_PASSPHRASE;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CHECK_AUTHENTICATION;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CREATING;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.IMPORTING;

public class PinPresenterImpl extends BaseFragmentPresenterImpl implements PinPresenter {

    private PinView mPinFragmentView;
    private PinInteractor mPinFragmentInteractor;
    private String pinRepeat;
    private String oldPin;
    private String pinHash;
    private PinAction mAction;
    private final Long mBanTime = 600000L;
    private int titleID = 0;
    private Subscription mSubscription;

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
        if (!mAction.equals(CREATING) || !mAction.equals(IMPORTING)) {
            Long banTime = getInteractor().getBanTime();
            Long currentTime = Calendar.getInstance().getTimeInMillis();
            Long remainingTimeOfBan = banTime - currentTime;
            if (remainingTimeOfBan > 0) {
                int min = (int) Math.ceil(remainingTimeOfBan / 60000.);
                getView().setAlertDialog(R.string.error, getInteractor().getBanPinString(min), R.string.cancel, BaseFragment.PopUpType.error);
                getView().clearPin();
                return;
            }
        }
        switch (mAction) {
            case CREATING: {
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
                            String passphrase = getInteractor().getRandomSeed();


                            getInteractor().savePassphraseSaltWithPin(pinRepeat, passphrase);
                            pinHash = getInteractor().generateSHA256String(pinRepeat);
                            if (getView().checkAvailabilityTouchId()) {
                                getInteractor().encodeInBackground(pinRepeat)
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
                                                getView().openTouchIDPreferenceFragment(false, pinRepeat);
                                                getView().dismissProgressDialog();
                                            }
                                        });
                            } else {
                                getInteractor().savePassword(pinHash);
                                getView().dismiss();
                                getView().openBackUpWalletFragment(true, pinRepeat);
                                getView().dismissProgressDialog();
                            }

                        } else {
                            getView().confirmError(R.string.incorrect_repeated_pin);
                        }
                        break;
                }
            }
            break;
            case IMPORTING: {
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
                            getInteractor().savePassphraseSaltWithPin(pinRepeat, getView().getPassphrase());
                            pinHash = getInteractor().generateSHA256String(pinRepeat);
                            if (getView().checkAvailabilityTouchId()) {
                                getInteractor().encodeInBackground(pinRepeat)
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
            case AUTHENTICATION: {
                try {
                    String pinHashEntered = getInteractor().generateSHA256String(pin);
                    String pinHashGenuine = getInteractor().getPassword();
                    boolean isCorrect = pinHashEntered.equals(pinHashGenuine);
                    changeBanState(isCorrect);
                    if (isCorrect) {
                        getView().clearError();
                        getView().setProgressDialog();
                        getView().hideKeyBoard();
                        getInteractor().loadWallet(pin)
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
                                    public void onNext(String seed) {
                                        isDataLoaded = true;
                                        getView().onLogin();
                                        getView().dismissProgressDialog();
                                        getView().openWalletMainFragment();
                                    }
                                });
                    } else {
                        getView().confirmError(R.string.incorrect_pin);
                    }
                } catch (Exception ignored) {
                }
            }
            break;
            case CHECK_AUTHENTICATION: {
                String pinHashEntered = getInteractor().generateSHA256String(pin);
                String pinHashGenuine = getInteractor().getPassword();
                boolean isCorrect = pinHashEntered.equals(pinHashGenuine);
                changeBanState(isCorrect);
                if (isCorrect) {
                    getView().clearError();
                    getView().hideKeyBoard();
                    getView().setCheckAuthenticationShowFlag(false);
                    getView().onBackPressed();
                } else {
                    getView().confirmError(R.string.incorrect_pin);
                }
            }
            break;
            case AUTHENTICATION_FOR_PASSPHRASE: {
                String pinHashEntered = getInteractor().generateSHA256String(pin);
                String pinHashGenuine = getInteractor().getPassword();
                boolean isCorrect = pinHashEntered.equals(pinHashGenuine);
                changeBanState(isCorrect);
                if (isCorrect) {
                    getView().clearError();
                    getView().hideKeyBoard();
                    getView().setCheckAuthenticationShowFlag(false);
                    getView().onBackPressed();
                    getView().openBackUpWalletFragment(false, pin);
                } else {
                    getView().confirmError(R.string.incorrect_pin);
                }
            }
            break;
            case AUTHENTICATION_AND_SEND: {
                String pinHashEntered = getInteractor().generateSHA256String(pin);
                String pinHashGenuine = getInteractor().getPassword();
                boolean isCorrect = pinHashEntered.equals(pinHashGenuine);
                changeBanState(isCorrect);
                if (isCorrect) {
                    getView().clearError();
                    final String address = getView().getAddressForSendAction();
                    final String amount = getView().getAmountForSendAction();
                    final String token = getView().getTokenForSendAction();
                    getView().setProgressDialog();
                    getView().hideKeyBoard();
                    getInteractor().loadWallet(pin)
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
                                public void onNext(String seed) {
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
            case CHANGING: {
                switch (currentState) {
                    case 0:
                        oldPin = pin;
                        String pinHashEntered = getInteractor().generateSHA256String(pin);
                        String pinHashGenuine = getInteractor().getPassword();
                        boolean isCorrect = pinHashEntered.equals(pinHashGenuine);
                        if (isCorrect) {
                            currentState = 1;
                            getView().setDigitPin(6);
                            getView().clearError();
                            updateState();
                        } else {
                            getView().confirmError(R.string.incorrect_pin);
                        }
                        changeBanState(isCorrect);
                        break;
                    case 1:
                        pinRepeat = pin;
                        currentState = 2;
                        updateState();
                        break;
                    case 2:
                        if (pin.equals(pinRepeat)) {
                            getView().clearError();
                            final String pinHash = getInteractor().generateSHA256String(pinRepeat);
                            getInteractor().savePassword(pinHash);
                            String passphrase = getInteractor().getUnSaltPassphrase(oldPin);
                            getInteractor().savePassphraseSaltWithPin(pinRepeat, passphrase);
                            if (getView().checkAvailabilityTouchId()) {
                                getInteractor().encodeInBackground(pinRepeat)
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
            case AUTHENTICATION:
            case AUTHENTICATION_AND_SEND:
            case CHECK_AUTHENTICATION:
            case IMPORTING:
                getView().onCancelClick();
                break;
            case AUTHENTICATION_FOR_PASSPHRASE:
            case CREATING:
            case CHANGING: {
                getView().onBackPressed();
                break;
            }
        }
    }

    @Override
    public void setAction(PinAction action) {
        mAction = action;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTouchIdFlag = getView().checkTouchIdEnable();
        switch (mAction) {
            case IMPORTING:
            case CREATING:
                getView().setDigitPin(6);
                titleID = R.string.create_pin;
                break;
            case AUTHENTICATION_AND_SEND:
            case AUTHENTICATION:
            case CHECK_AUTHENTICATION:
            case AUTHENTICATION_FOR_PASSPHRASE:
                if (mTouchIdFlag) {
                    getView().checkSensorState(new MainActivity.SensorStateListener() {
                        @Override
                        public void onRequest(MainActivity.SensorState sensorState) {
                            if (sensorState == MainActivity.SensorState.READY) {
                                titleID = R.string.confirm_fingerprint_or_pin;
                            } else {
                                titleID = R.string.enter_pin;
                            }
                        }
                    });
                } else {
                    titleID = R.string.enter_pin;
                }
                if (getInteractor().getSixDigitPassword().isEmpty()) {
                    getView().setDigitPin(4);
                } else {
                    getView().setDigitPin(6);
                }
                break;
            case CHANGING:
                titleID = R.string.change_pin;
                if (getInteractor().getSixDigitPassword().isEmpty()) {
                    getView().setDigitPin(4);
                } else {
                    getView().setDigitPin(6);
                }
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
                case CREATING: {
                    if (pinHash != null) {
                        getInteractor().savePassword(pinHash);
                        getView().dismissProgressDialog();
                        getView().openBackUpWalletFragment(true, pinRepeat);
                    }
                    break;
                }
                case AUTHENTICATION: {
                    getView().openWalletMainFragment();
                    getView().dismissProgressDialog();
                    break;
                }
                case AUTHENTICATION_AND_SEND: {
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
        if (mTouchIdFlag && (mAction.equals(AUTHENTICATION_AND_SEND) || mAction.equals(AUTHENTICATION) || mAction.equals(CHECK_AUTHENTICATION) || mAction.equals(AUTHENTICATION_FOR_PASSPHRASE))) {
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
            case IMPORTING:
            case CREATING:
                state = CREATING_STATE[currentState];
                break;
            case AUTHENTICATION:
            case AUTHENTICATION_AND_SEND:
            case CHECK_AUTHENTICATION:
            case AUTHENTICATION_FOR_PASSPHRASE:
                state = AUTHENTICATION_STATE[currentState];
                break;
            case CHANGING:
                state = CHANGING_STATE[currentState];
                break;
        }
        getView().updateState(state);
    }

    @Override
    public void onAuthenticationSucceeded(Cipher cipher) {
        String encoded = getInteractor().getTouchIdPassword();
        String decoded = getInteractor().decode(encoded, cipher);
        getView().setPin(decoded);
    }

    private void changeBanState(boolean isCorrect) {
        int failedAttemptsCount;
        if (isCorrect) {
            failedAttemptsCount = 0;
        } else {
            failedAttemptsCount = getInteractor().getFailedAttemptsCount();
            failedAttemptsCount++;
            if (failedAttemptsCount == 3) {
                Long currentTime = Calendar.getInstance().getTimeInMillis();
                Long banTime = currentTime + mBanTime;
                getInteractor().setBanTime(banTime);
                failedAttemptsCount = 0;
                getView().setAlertDialog(R.string.error, getInteractor().getBanPinString(mBanTime.intValue() / 60000), R.string.cancel, BaseFragment.PopUpType.error);
            }
        }
        getInteractor().setFailedAttemptsCount(failedAttemptsCount);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}