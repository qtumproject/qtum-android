package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;


public class PinFragmentPresenterImpl extends BaseFragmentPresenterImpl implements PinFragmentPresenter {

    private PinFragmentView mPinFragmentView;
    private PinFragmentInteractorImpl mPinFragmentInteractor;

    public PinFragmentPresenterImpl(PinFragmentView pinFragmentView) {
        mPinFragmentView = pinFragmentView;
        mPinFragmentInteractor = new PinFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public void confirm(String[] password, String action) {
        switch (action) {
            case PinFragment.CREATING: {
                if (password[0].length() < 4) {
                    getView().confirmError(getView().getContext().getString(R.string.pin_is_not_long_enough));
                } else {
                    int intPassword = Integer.parseInt(password[0]);
                    WalletFragment walletFragment = WalletFragment.newInstance();
                    getInteractor().savePassword(intPassword);
                    getInteractor().generateAndSavePubKey();
                    getView().openFragment(walletFragment);
                    getView().hideKeyBoard();
                }
                break;
            }
            case PinFragment.AUTHENTICATION: {
                if (password[0].length() < 4) {
                    getView().confirmError(getView().getContext().getString(R.string.pin_is_not_long_enough));
                } else {
                    int intPassword = Integer.parseInt(password[0]);
                    if (intPassword == getInteractor().getPassword()) {
                        WalletFragment walletFragment = WalletFragment.newInstance();
                        getView().openFragment(walletFragment);
                        getView().hideKeyBoard();
                    } else {
                        getView().confirmError(getView().getContext().getString(R.string.incorrect_password));
                    }
                }
                break;
            }
            case PinFragment.CHANGING: {
                getView().clearErrors();
                if (password[0].length() < 4) {
                    getView().confirmError(getView().getContext().getString(R.string.pin_is_not_long_enough));
                } else {
                    int intPassword = Integer.parseInt(password[0]);
                    if (intPassword == getInteractor().getPassword()) {
                        if (password[1].length() < 4) {
                            getView().confirmChangePinError(getView().getContext().getString(R.string.pin_is_not_long_enough), "");
                        } else {
                            int intPasswordNew = Integer.parseInt(password[1]);
                            int intPasswordNewRepeat;
                            if (password[2].length() < 4) {
                                intPasswordNewRepeat = 0;
                            } else {
                                intPasswordNewRepeat = Integer.parseInt(password[2]);
                            }
                            if (intPasswordNew == intPasswordNewRepeat) {
                                getInteractor().savePassword(intPasswordNew);
                                getView().hideKeyBoard();
                                getView().getFragmentActivity().onBackPressed();
                            } else {
                                getView().confirmChangePinError("", getView().getContext().getResources().getString(R.string.incorrect_repeated_pin));
                            }
                        }
                    } else {
                        getView().confirmError(getView().getContext().getString(R.string.incorrect_password));
                    }
                }
                break;
            }
        }
    }

    @Override
    public void cancel(String action) {
        switch (action) {
            case PinFragment.CREATING: {

            }
            case PinFragment.AUTHENTICATION: {
                getView().finish();
                break;
            }
            case PinFragment.CHANGING: {
                getView().getFragmentActivity().onBackPressed();
                break;
            }
        }
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        ((MainActivity) getView().getFragmentActivity()).hideBottomNavigationView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getView().getFragmentActivity()).showBottomNavigationView();
    }

    @Override
    public PinFragmentView getView() {
        return mPinFragmentView;
    }

    public PinFragmentInteractorImpl getInteractor() {
        return mPinFragmentInteractor;
    }
}
