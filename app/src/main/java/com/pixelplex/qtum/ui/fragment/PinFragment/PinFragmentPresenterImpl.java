package com.pixelplex.qtum.ui.fragment.PinFragment;


import android.content.Context;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import com.pixelplex.qtum.ui.fragment.WalletFragment.WalletFragment;
import com.pixelplex.qtum.ui.fragment.WalletMainFragment.WalletMainFragment;


class PinFragmentPresenterImpl extends BaseFragmentPresenterImpl implements PinFragmentPresenter {

    private PinFragmentView mPinFragmentView;
    private PinFragmentInteractorImpl mPinFragmentInteractor;
    private int pinForRepeat;
    private String mAction;

    private String[] CREATING_STATE;
    private String[] AUTHENTICATION_STATE;
    private String[] CHANGING_STATE;

    private int currentState = 0;

    PinFragmentPresenterImpl(PinFragmentView pinFragmentView) {
        mPinFragmentView = pinFragmentView;

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
                        pinForRepeat = Integer.parseInt(pin);
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (Integer.parseInt(pin) == pinForRepeat) {
                            getView().clearError();
                            final BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance(true);
                            getView().setProgressDialog(getView().getContext().getString(R.string.loading));
                            getView().hideKeyBoard();
                            getInteractor().createWallet(getView().getContext(), new PinFragmentInteractorImpl.CreateWalletCallBack() {
                                @Override
                                public void onSuccess() {
                                    getInteractor().savePassword(pinForRepeat);
                                    ((MainActivity) getView().getFragmentActivity()).setAuthenticationFlag(true);
                                    getView().openRootFragment(backUpWalletFragment);
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
                        pinForRepeat = Integer.parseInt(pin);
                        currentState = 1;
                        getView().clearError();
                        updateState();
                        break;
                    case 1:
                        if (Integer.parseInt(pin) == pinForRepeat) {
                            getView().clearError();
                            final WalletMainFragment walletFragment = WalletMainFragment.newInstance();
                            getInteractor().savePassword(pinForRepeat);
                            getInteractor().setKeyGeneratedInstance(true);
                            ((MainActivity) getView().getFragmentActivity()).setRootFragment(walletFragment);
                            ((MainActivity) getView().getFragmentActivity()).setAuthenticationFlag(true);
                            getView().openRootFragment(walletFragment);
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
                        }
                        break;
                }
            }
            break;

            case PinFragment.AUTHENTICATION: {
                int intPassword = Integer.parseInt(pin);
                if (intPassword == getInteractor().getPassword()) {
                    getView().clearError();
                    final WalletMainFragment walletFragment = WalletMainFragment.newInstance();
                    getView().setProgressDialog(getView().getContext().getString(R.string.loading));
                    getView().hideKeyBoard();
                    getInteractor().loadWalletFromFile(new PinFragmentInteractorImpl.LoadWalletFromFileCallBack() {
                        @Override
                        public void onSuccess() {
                            ((MainActivity) getView().getFragmentActivity()).setRootFragment(walletFragment);
                            ((MainActivity) getView().getFragmentActivity()).setAuthenticationFlag(true);
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

            case PinFragment.AUTHENTICATION_AND_SEND: {
                int intPassword = Integer.parseInt(pin);
                if (intPassword == getInteractor().getPassword()) {
                    getView().clearError();
                    String address = ((MainActivity) getView().getFragmentActivity()).getAddressForSendAction();
                    String amount = ((MainActivity) getView().getFragmentActivity()).getAmountForSendAction();
                    final SendBaseFragment sendBaseFragment = SendBaseFragment.newInstance(false,address,amount);
                    getView().setProgressDialog(getView().getContext().getString(R.string.loading));
                    getView().hideKeyBoard();
                    getInteractor().loadWalletFromFile(new PinFragmentInteractorImpl.LoadWalletFromFileCallBack() {
                        @Override
                        public void onSuccess() {
                            ((MainActivity) getView().getFragmentActivity()).setRootFragment(sendBaseFragment);
                            ((MainActivity) getView().getFragmentActivity()).setAuthenticationFlag(true);
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
                        int intPassword = Integer.parseInt(pin);
                        if (intPassword == getInteractor().getPassword()) {
                            currentState = 1;
                            getView().clearError();
                            updateState();
                        } else {
                            getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                        }
                        break;
                    case 1:
                        pinForRepeat = Integer.parseInt(pin);
                        currentState = 2;
                        getView().clearError();
                        updateState();
                        break;
                    case 2:
                        if (Integer.parseInt(pin) == pinForRepeat) {
                            getView().clearError();
                            getInteractor().savePassword(Integer.parseInt(pin));
                            getView().getFragmentActivity().onBackPressed();
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
            case PinFragment.AUTHENTICATION_AND_SEND:{
                getView().finish();
                break;
            }
            case PinFragment.CREATING:
            case PinFragment.IMPORTING:
            case PinFragment.CHANGING: {
                getView().getFragmentActivity().onBackPressed();
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
        int titleID = 0;
        switch (mAction) {
            case PinFragment.IMPORTING:
            case PinFragment.CREATING:
                titleID = R.string.create_pin;
                break;
            case PinFragment.AUTHENTICATION_AND_SEND:
            case PinFragment.AUTHENTICATION:
                titleID = R.string.enter_pin;
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
        pinForRepeat = 0;
        currentState = 0;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        updateState();
        ((MainActivity) getView().getFragmentActivity()).hideBottomNavigationView(true);
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
                    ((MainActivity) getView().getFragmentActivity()).setRootFragment(walletFragment);
                    getView().openRootFragment(walletFragment);
                    getView().dismissProgressDialog();
                    break;
                }
                case PinFragment.AUTHENTICATION_AND_SEND: {
                    String address = ((MainActivity) getView().getFragmentActivity()).getAddressForSendAction();
                    String amount = ((MainActivity) getView().getFragmentActivity()).getAmountForSendAction();
                    final SendBaseFragment sendBaseFragment = SendBaseFragment.newInstance(false,address,amount);
                    ((MainActivity) getView().getFragmentActivity()).setRootFragment(sendBaseFragment);
                    getView().openRootFragment(sendBaseFragment);
                    getView().dismissProgressDialog();
                    break;
                }
            }
            PinFragmentInteractorImpl.isDataLoaded = false;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAction.equals(PinFragment.CHANGING)) {
            ((MainActivity) getView().getFragmentActivity()).showBottomNavigationView(true);
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
                state = AUTHENTICATION_STATE[currentState];
                break;
            case PinFragment.CHANGING:
                state = CHANGING_STATE[currentState];
                break;
        }
        getView().updateState(state);
    }
}