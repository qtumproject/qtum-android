package com.pixelplex.qtum.ui.fragment.PinFragment;



import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.widget.Toast;

import com.pixelplex.qtum.R;

import com.pixelplex.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import com.pixelplex.qtum.ui.fragment.TouchIDPreferenceFragment.TouchIDPreferenceFragment;
import com.pixelplex.qtum.ui.fragment.WalletMainFragment.WalletMainFragment;
import com.pixelplex.qtum.utils.CryptoUtils;
import com.pixelplex.qtum.utils.FingerprintUtils;

import javax.crypto.Cipher;



class PinFragmentPresenterImpl extends BaseFragmentPresenterImpl implements PinFragmentPresenter {

    private PinFragmentView mPinFragmentView;
    private PinFragmentInteractorImpl mPinFragmentInteractor;
    private String pinForRepeat;
    private String mAction;
    private Context mContext;

    private FingerprintHelper mFingerprintHelper;

    private String[] CREATING_STATE;
    private String[] AUTHENTICATION_STATE;
    private String[] CHANGING_STATE;

    private int currentState = 0;
    private boolean mTouchIdFlag;

    PinFragmentPresenterImpl(PinFragmentView pinFragmentView) {
        mPinFragmentView = pinFragmentView;
        mContext = getView().getContext();
        String ENTER_PIN = getView().getContext().getString(R.string.enter_pin_lower_case);
        String ENTER_NEW_PIN = getView().getContext().getString(R.string.enter_new_pin);
        String REPEAT_PIN = getView().getContext().getString(R.string.repeat_pin);
        String ENTER_OLD_PIN = getView().getContext().getString(R.string.enter_old_pin);
        CREATING_STATE = new String[]{ENTER_NEW_PIN, REPEAT_PIN};
        AUTHENTICATION_STATE = new String[]{ENTER_PIN};
        CHANGING_STATE = new String[]{ENTER_OLD_PIN, ENTER_NEW_PIN, REPEAT_PIN};

        mPinFragmentInteractor = new PinFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public void confirm(String pin) {
        switch (mAction) {
            case PinFragment.CREATING: {
                switch (currentState) {
                    case 0:
                        pinForRepeat = pin;
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (pin.equals(pinForRepeat) ) {
                            getView().clearError();
                            getView().setProgressDialog();
                            getView().hideKeyBoard();
                            getInteractor().createWallet(getView().getContext(), new PinFragmentInteractorImpl.CreateWalletCallBack() {
                                @Override
                                public void onSuccess() {
                                    Fragment fragment;
                                    if(getView().getMainActivity().checkTouchId()) {
                                        fragment = TouchIDPreferenceFragment.newInstance(false);
                                    } else {
                                        fragment = BackUpWalletFragment.newInstance(true);
                                    }
                                    if(mTouchIdFlag){
                                        getInteractor().saveTouchIdPassword(CryptoUtils.encode(pinForRepeat));
                                    }
                                    getInteractor().savePassword(pinForRepeat);
                                    getView().getMainActivity().setAuthenticationFlag(true);
                                    getView().openRootFragment(fragment);
                                    getView().dismissProgressDialog();
                                    PinFragmentInteractorImpl.isDataLoaded = false;
                                }
                            });
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
                        }
                        break;
                }
            }
            break;

            case PinFragment.IMPORTING: {
                switch (currentState) {
                    case 0:
                        pinForRepeat = pin;
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (pin.equals(pinForRepeat)) {
                            getView().clearError();
                            getInteractor().savePassword(pinForRepeat);
                            if(mTouchIdFlag){
                                getInteractor().saveTouchIdPassword(CryptoUtils.encode(pinForRepeat));
                            }
                            getInteractor().setKeyGeneratedInstance(true);
                            Fragment fragment;
                            if(getView().getMainActivity().checkTouchId()) {
                                fragment = TouchIDPreferenceFragment.newInstance(true);
                            } else {
                                fragment = WalletMainFragment.newInstance();
                                getView().getMainActivity().setRootFragment(fragment);
                            }
                            getView().getMainActivity().setAuthenticationFlag(true);
                            getView().openRootFragment(fragment);
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
                        }
                        break;
                }
            }
            break;

            case PinFragment.AUTHENTICATION: {
                if (pin.equals(getInteractor().getPassword())) {
                    getView().clearError();
                    final WalletMainFragment walletFragment = WalletMainFragment.newInstance();
                    getView().setProgressDialog();
                    getView().hideKeyBoard();
                    getInteractor().loadWalletFromFile(new PinFragmentInteractorImpl.LoadWalletFromFileCallBack() {
                        @Override
                        public void onSuccess() {
                            getView().getMainActivity().setRootFragment(walletFragment);
                            getView().getMainActivity().setAuthenticationFlag(true);
                            getView().openRootFragment(walletFragment);
                            getView().dismissProgressDialog();
                            PinFragmentInteractorImpl.isDataLoaded = false;
                        }
                    });
                } else {
                    getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                }
            }
            break;

            case PinFragment.CHECK_AUTHENTICATION: {
                if (pin.equals(getInteractor().getPassword())) {
                    getView().clearError();
                    getView().hideKeyBoard();
                    getView().getMainActivity().setCheckAuthenticationShowFlag(false);
                    getView().dismissPinFragment();
                } else {
                    getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                }
            }
            break;

            case PinFragment.AUTHENTICATION_AND_SEND: {
                if (pin.equals(getInteractor().getPassword())) {
                    getView().clearError();
                    String address = getView().getMainActivity().getAddressForSendAction();
                    String amount = getView().getMainActivity().getAmountForSendAction();
                    final SendBaseFragment sendBaseFragment = SendBaseFragment.newInstance(false, address, amount);
                    getView().setProgressDialog();
                    getView().hideKeyBoard();
                    getInteractor().loadWalletFromFile(new PinFragmentInteractorImpl.LoadWalletFromFileCallBack() {
                        @Override
                        public void onSuccess() {
                            getView().getMainActivity().setRootFragment(sendBaseFragment);
                            getView().getMainActivity().setAuthenticationFlag(true);
                            getView().openRootFragment(sendBaseFragment);
                            getView().dismissProgressDialog();
                            PinFragmentInteractorImpl.isDataLoaded = false;
                        }
                    });
                } else {
                    getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                }
            }
            break;

            case PinFragment.CHANGING: {
                switch (currentState) {
                    case 0:
                        if (pin.equals(getInteractor().getPassword())) {
                            currentState = 1;
                            getView().clearError();
                            updateState();
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                        }
                        break;
                    case 1:
                        pinForRepeat = pin;
                        currentState = 2;
                        getView().clearError();
                        updateState();
                        break;
                    case 2:
                        if (pin.equals(pinForRepeat)) {
                            getView().clearError();
                            getInteractor().savePassword(pin);
                            if(mTouchIdFlag){
                                getInteractor().saveTouchIdPassword(CryptoUtils.encode(pin));
                            }
                            getView().getMainActivity().onBackPressed();
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
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
            case PinFragment.CHECK_AUTHENTICATION:{
                getView().finish();
                break;
            }
            case PinFragment.CREATING:
            case PinFragment.IMPORTING:
            case PinFragment.CHANGING: {
                getView().getMainActivity().onBackPressed();
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
        mTouchIdFlag = getView().getMainActivity().checkTouchId();
        int titleID = 0;
        switch (mAction) {
            case PinFragment.IMPORTING:
            case PinFragment.CREATING:
                titleID = R.string.create_pin;
                break;
            case PinFragment.AUTHENTICATION_AND_SEND:
            case PinFragment.AUTHENTICATION:
            case PinFragment.CHECK_AUTHENTICATION:
                if(mTouchIdFlag){
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
    public void onPause(Context context) {
        super.onPause(context);
        pinForRepeat = "0";
        currentState = 0;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        updateState();
        getView().getMainActivity().hideBottomNavigationView(true);
        if (PinFragmentInteractorImpl.isDataLoaded) {
            switch (mAction) {
                case PinFragment.CREATING: {
                    BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance(true);
                    getInteractor().savePassword(pinForRepeat);
                    getView().openRootFragment(backUpWalletFragment);
                    getView().dismissProgressDialog();
                    break;
                }
                case PinFragment.AUTHENTICATION: {
                    WalletMainFragment walletFragment = WalletMainFragment.newInstance();
                    getView().getMainActivity().setRootFragment(walletFragment);
                    getView().openRootFragment(walletFragment);
                    getView().dismissProgressDialog();
                    break;
                }
                case PinFragment.AUTHENTICATION_AND_SEND: {
                    String address = getView().getMainActivity().getAddressForSendAction();
                    String amount = getView().getMainActivity().getAmountForSendAction();
                    final SendBaseFragment sendBaseFragment = SendBaseFragment.newInstance(false, address, amount);
                    getView().getMainActivity().setRootFragment(sendBaseFragment);
                    getView().openRootFragment(sendBaseFragment);
                    getView().dismissProgressDialog();
                    break;
                }
            }
            PinFragmentInteractorImpl.isDataLoaded = false;
        }

        if(mTouchIdFlag && (mAction.equals(PinFragment.AUTHENTICATION_AND_SEND) || mAction.equals(PinFragment.AUTHENTICATION) || mAction.equals(PinFragment.CHECK_AUTHENTICATION))){
            prepareSensor();
        }

    }

    private void prepareSensor() {
        if (FingerprintUtils.isSensorStateAt(FingerprintUtils.mSensorState.READY, mContext)) {
            FingerprintManagerCompat.CryptoObject cryptoObject = CryptoUtils.getCryptoObject();
            if (cryptoObject != null) {
                mFingerprintHelper = new FingerprintHelper(mContext);
                mFingerprintHelper.startAuth(cryptoObject);
            } else {
                //TODO: make
                Toast.makeText(mContext, "new fingerprint enrolled. enter pin again", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onStop(Context context) {
        super.onStop(context);
        if (mFingerprintHelper != null) {
            mFingerprintHelper.cancel();
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
            getView().confirmError(errString.toString());
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            getView().confirmError(helpString.toString());
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Cipher cipher = result.getCryptoObject().getCipher();
            String encoded = getInteractor().getTouchIdPassword();
            String decoded = CryptoUtils.decode(encoded, cipher);
            getView().setPin(decoded);
        }

        @Override
        public void onAuthenticationFailed() {
            getView().confirmError("try again");
        }

    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAction.equals(PinFragment.CHANGING) || mAction.equals(PinFragment.CHECK_AUTHENTICATION)) {
            getView().getMainActivity().showBottomNavigationView(true);
        }
    }

    @Override
    public PinFragmentView getView() {
        return mPinFragmentView;
    }

    public PinFragmentInteractorImpl getInteractor() {
        return mPinFragmentInteractor;
    }

    private void updateState() {
        String state = null;
        switch (mAction) {
            case PinFragment.IMPORTING:
            case PinFragment.CREATING:
                state = CREATING_STATE[currentState];
                break;
            case PinFragment.AUTHENTICATION:
            case PinFragment.AUTHENTICATION_AND_SEND:
            case PinFragment.CHECK_AUTHENTICATION:
                state = AUTHENTICATION_STATE[currentState];
                break;
            case PinFragment.CHANGING:
                state = CHANGING_STATE[currentState];
                break;
        }
        getView().updateState(state);
    }

}