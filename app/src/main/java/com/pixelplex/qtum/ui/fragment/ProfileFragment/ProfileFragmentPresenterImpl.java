package com.pixelplex.qtum.ui.fragment.ProfileFragment;


import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.datastorage.LanguageChangeListener;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.CurrencyFragment.CurrencyFragment;
import com.pixelplex.qtum.ui.fragment.LanguageFragment.LanguageFragment;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;
import com.pixelplex.qtum.ui.fragment.SetTokenNameFragment.SetTokenNameFragment;
import com.pixelplex.qtum.ui.fragment.StartPageFragment.StartPageFragment;


class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    private UpdateService mUpdateService;

    private ProfileFragmentView mProfileFragmentView;
    private ProfileFragmentInteractorImpl mProfileFragmentInteractor;
    private LanguageChangeListener mLanguageChangeListener;

    ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView) {
        mProfileFragmentView = profileFragmentView;
        mProfileFragmentInteractor = new ProfileFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mLanguageChangeListener = new LanguageChangeListener() {
            @Override
            public void onLanguageChange() {
                getView().resetText();
            }
        };
        QtumSharedPreference.getInstance().addLanguageListener(mLanguageChangeListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QtumSharedPreference.getInstance().removeLanguageListener(mLanguageChangeListener);
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

    public void onLanguageClick(){
        LanguageFragment languageFragment = LanguageFragment.newInstance();
        getView().openFragment(languageFragment);
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