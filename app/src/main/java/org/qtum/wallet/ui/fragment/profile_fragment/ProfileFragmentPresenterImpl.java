package org.qtum.wallet.ui.fragment.profile_fragment;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.about_fragment.AboutFragment;
import org.qtum.wallet.ui.fragment.language_fragment.LanguageFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;
import org.qtum.wallet.ui.fragment.smart_contracts_fragment.SmartContractsFragment;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensFragment;
import org.qtum.wallet.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    private UpdateService mUpdateService;

    private ProfileFragmentView mProfileFragmentView;
    private ProfileFragmentInteractorImpl mProfileFragmentInteractor;
    private LanguageChangeListener mLanguageChangeListener;

    private static List<SettingObject> settingsData;

    public ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView) {
        mProfileFragmentView = profileFragmentView;
        mProfileFragmentInteractor = new ProfileFragmentInteractorImpl(getView().getContext());
        initSettingsData();
    }

    private void initSettingsData() {

        if(settingsData == null) {
            settingsData = new ArrayList<>();
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.language, org.qtum.wallet.R.drawable.ic_language, 0));
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.change_pin, org.qtum.wallet.R.drawable.ic_changepin, 1));
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.wallet_backup, org.qtum.wallet.R.drawable.ic_backup, 1));
            if(getView().getMainActivity().checkAvailabilityTouchId()) {
                settingsData.add(new SettingSwitchObject(org.qtum.wallet.R.string.touch_id, org.qtum.wallet.R.drawable.ic_touchid, 1, QtumSharedPreference.getInstance().isTouchIdEnable(getView().getContext())));
            }
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.smart_contracts, org.qtum.wallet.R.drawable.ic_prof_smartcontr, 2));
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.subscribe_tokens, org.qtum.wallet.R.drawable.ic_tokensubscribe, 2));
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.about, org.qtum.wallet.R.drawable.ic_about, 4));
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.switch_theme, org.qtum.wallet.R.drawable.ic_prof_theme, 4));
            settingsData.add(new SettingObject(org.qtum.wallet.R.string.log_out, org.qtum.wallet.R.drawable.ic_logout, 4));
        }
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
        getView().getMainActivity().setIconChecked(1);
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

    private ProfileFragmentInteractorImpl getInteractor() {
        return mProfileFragmentInteractor;
    }

    @Override
    public void onChangePinClick() {
        BaseFragment pinFragment = PinFragment.newInstance(PinFragment.CHANGING, getView().getContext());
        getView().openFragment(pinFragment);
    }

    @Override
    public void onLogOutClick() {
        getView().startDialogFragmentForResult();
    }

    @Override
    public void onWalletBackUpClick() {
        BaseFragment fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION_FOR_PASSPHRASE,getView().getContext());
        getView().openFragment(fragment);
    }

    @Override
    public void onLogOutYesClick() {
        getInteractor().clearWallet();
        getView().getMainActivity().onLogout();
        mUpdateService = getView().getMainActivity().getUpdateService();
        mUpdateService.stopMonitoring();
        BaseFragment startPageFragment = StartPageFragment.newInstance(false, getView().getContext());
        getView().getMainActivity().openRootFragment(startPageFragment);
    }

    public void onLanguageClick(){
        BaseFragment languageFragment = LanguageFragment.newInstance(getView().getContext());
        getView().openFragment(languageFragment);
    }

    @Override
    public void onSmartContractsClick() {
        BaseFragment smartContractsFragment = SmartContractsFragment.newInstance(getView().getContext());
        getView().openFragment(smartContractsFragment);
    }

    @Override
    public void onSubscribeTokensClick() {
        BaseFragment subscribeTokensFragment = SubscribeTokensFragment.newInstance(getView().getContext());
        getView().openFragment(subscribeTokensFragment);
    }

    @Override
    public void onAboutClick() {
        BaseFragment aboutFragment = AboutFragment.newInstance(getView().getContext());
        getView().openFragment(aboutFragment);
    }

    public void onTouchIdSwitched(boolean isChecked){
        QtumSharedPreference.getInstance().saveTouchIdEnable(getView().getContext(),isChecked);
    }

    public void switchTheme() {
        ThemeUtils.switchPreferencesTheme(getView().getContext());
        getView().getMainActivity().reloadActivity();
    }
}