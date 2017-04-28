package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;


import org.qtum.mromanovsky.qtum.dataprovider.UpdateService;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.CurrencyFragment.CurrencyFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenNameFragment.SetTokenNameFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment.StartPageFragment;


class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    private UpdateService mUpdateService;

    private ProfileFragmentView mProfileFragmentView;
    private ProfileFragmentInteractorImpl mProfileFragmentInteractor;

    ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView) {
        mProfileFragmentView = profileFragmentView;
        mProfileFragmentInteractor = new ProfileFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public ProfileFragmentView getView() {
        return mProfileFragmentView;
    }

    public ProfileFragmentInteractorImpl getInteractor() {
        return mProfileFragmentInteractor;
    }

    @Override
    public void onChangePinClick() {
        PinFragment pinFragment = PinFragment.newInstance(PinFragment.CHANGING);
        getView().openFragment(pinFragment);
    }

    @Override
    public void onLogOutClick() {
        getView().startDialogFragmentForResult();
    }



    @Override
    public void onWalletBackUpClick() {
        BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance(false);
        getView().openFragment(backUpWalletFragment);
    }

    @Override
    public void onLogOutYesClick() {
        getInteractor().clearWallet();
        ((MainActivity) getView().getFragmentActivity()).setAuthenticationFlag(false);
        mUpdateService = ((MainActivity) getView().getFragmentActivity()).getUpdateService();
        mUpdateService.stopMonitoring();

        StartPageFragment startPageFragment = StartPageFragment.newInstance();
        ((MainActivity)getView().getFragmentActivity()).openRootFragment(startPageFragment);
        ((MainActivity)getView().getFragmentActivity()).setIconChecked(0);
    }

    @Override
    public void onCreateTokenClick() {
        SetTokenNameFragment setTokenNameFragment = SetTokenNameFragment.newInstance();
        getView().openFragment(setTokenNameFragment);
    }

    @Override
    public void onSubscribeTokensClick() {
        CurrencyFragment currencyFragment = CurrencyFragment.newInstance(false);
        getView().openFragment(currencyFragment);
    }

}