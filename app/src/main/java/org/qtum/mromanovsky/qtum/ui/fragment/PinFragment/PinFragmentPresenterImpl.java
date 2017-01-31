package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import android.content.Context;

import org.bitcoinj.wallet.Wallet;
import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;


public class PinFragmentPresenterImpl extends BaseFragmentPresenterImpl implements PinFragmentPresenter {

    private PinFragmentView mPinFragmentView;
    private PinFragmentInteractorImpl mPinFragmentInteractor;
    private int pinForRepeat;

    public PinFragmentPresenterImpl(PinFragmentView pinFragmentView) {
        mPinFragmentView = pinFragmentView;
        mPinFragmentInteractor = new PinFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public void confirm(String pin) {
        switch (PinFragment.sAction) {
            case PinFragment.CREATING: {
                if (pin.length() < 4) {
                    getView().confirmError(getView().getContext().getString(R.string.pin_is_not_long_enough));
                } else {
                    switch (PinFragment.currentState){
                        case 0:
                            pinForRepeat = Integer.parseInt(pin);
                            PinFragment.currentState=1;
                            getView().clearError();
                            getView().updateState();
                            break;
                        case 1:
                            if(Integer.parseInt(pin) == pinForRepeat) {
                                getView().clearError();
                                final WalletFragment walletFragment = WalletFragment.newInstance();
                                getView().setDialogProgressBar("Key generation");
                                getInteractor().generateRegisterKeyAndID(getView().getContext(), new PinFragmentInteractorImpl.GenerateRegisterKeyAndIdentifierCallBack() {
                                    @Override
                                    public void onSuccess(String[] keyAndIdentifier) {
                                        getInteractor().savePassword(pinForRepeat);
                                        getView().openFragment(walletFragment);
                                        getView().dismissDialogProgressBar();
                                    }
                                });
                            } else {
                                getView().confirmError(getView().getContext().getString(R.string.incorrect_repeated_pin));
                            }
                            break;
                    }
                }
                break;
            }
            case PinFragment.AUTHENTICATION: {
                if (pin.length() < 4) {
                    getView().confirmError(getView().getContext().getString(R.string.pin_is_not_long_enough));
                } else {
                    int intPassword = Integer.parseInt(pin);
                    if (intPassword == getInteractor().getPassword()) {
                        getView().clearError();
                        final WalletFragment walletFragment = WalletFragment.newInstance();
                        getView().setDialogProgressBar("Loading key");
                        getInteractor().getWalletFromFile(new PinFragmentInteractorImpl.GetWalletFromFileCallBack() {
                            @Override
                            public void onSuccess(Wallet wallet) {
                                getView().openFragment(walletFragment);
                                getView().dismissDialogProgressBar();
                            }
                        });
                    } else {
                        getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                    }
                }
                break;
            }
            case PinFragment.CHANGING: {

                if (pin.length() < 4) {
                    getView().confirmError(getView().getContext().getString(R.string.pin_is_not_long_enough));
                } else {
                    switch (PinFragment.currentState){
                        case 0:
                            int intPassword = Integer.parseInt(pin);
                            if (intPassword == getInteractor().getPassword()){
                                PinFragment.currentState=1;
                                getView().clearError();
                                getView().updateState();
                            } else {
                                getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                            }
                            break;
                        case 1:
                            pinForRepeat = Integer.parseInt(pin);
                            PinFragment.currentState=2;
                            getView().clearError();
                            getView().updateState();
                            break;
                        case 2:
                            if(Integer.parseInt(pin) == pinForRepeat) {
                                getView().clearError();
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
    }


    @Override
    public void cancel() {
        switch (PinFragment.sAction) {

            case PinFragment.AUTHENTICATION: {
                getView().finish();
                break;
            }
            case PinFragment.CREATING: {

            }
            case PinFragment.CHANGING: {
                getView().getFragmentActivity().onBackPressed();
                break;
            }
        }
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        getView().hideKeyBoard();
        pinForRepeat = 0;
        PinFragment.currentState = 0;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        getView().updateState();
        ((MainActivity) getView().getFragmentActivity()).hideBottomNavigationView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(PinFragment.sAction==PinFragment.CHANGING) {
            ((MainActivity) getView().getFragmentActivity()).showBottomNavigationView();
        }
    }

    @Override
    public PinFragmentView getView() {
        return mPinFragmentView;
    }

    public PinFragmentInteractorImpl getInteractor() {
        return mPinFragmentInteractor;
    }
}