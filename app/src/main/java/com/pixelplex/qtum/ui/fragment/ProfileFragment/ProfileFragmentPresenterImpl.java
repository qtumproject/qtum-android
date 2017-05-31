package com.pixelplex.qtum.ui.fragment.ProfileFragment;


import com.pixelplex.qtum.R;
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
import com.pixelplex.qtum.ui.fragment.SmartContractListFragment.SmartContractListFragment;
import com.pixelplex.qtum.ui.fragment.StartPageFragment.StartPageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    private UpdateService mUpdateService;

    private ProfileFragmentView mProfileFragmentView;
    private ProfileFragmentInteractorImpl mProfileFragmentInteractor;
    private LanguageChangeListener mLanguageChangeListener;

    List<SettingObject> settingsData;

    ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView) {
        mProfileFragmentView = profileFragmentView;
        mProfileFragmentInteractor = new ProfileFragmentInteractorImpl(getView().getContext());
        initSettingsData();
    }

    private void initSettingsData() {
        settingsData = new ArrayList<>();
        settingsData.add(new SettingObject(R.string.language, R.drawable.ic_language, 0));
        settingsData.add(new SettingObject(R.string.change_pin, R.drawable.ic_changepin, 1));
        settingsData.add(new SettingObject(R.string.wallet_back_up, R.drawable.ic_backup, 1));
        settingsData.add(new SettingObject(R.string.subscribe_tokens,R.drawable.ic_tokensubscribe,2));
        settingsData.add(new SettingObject(R.string.smart_contracts,R.drawable.ic_tokencreate,2));
        settingsData.add(new SettingObject(R.string.about,R.drawable.ic_about,3));
        settingsData.add(new SettingObject(R.string.log_out, R.drawable.ic_logout, 3));
    }

    public List<SettingObject> getSettingsData () {
        return settingsData;
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
        SmartContractListFragment smartContractListFragment = SmartContractListFragment.newInstance();
        getView().openFragment(smartContractListFragment);
    }

    @Override
    public void onSubscribeTokensClick() {
        CurrencyFragment currencyFragment = CurrencyFragment.newInstance(false);
        getView().openFragment(currencyFragment);
    }

}