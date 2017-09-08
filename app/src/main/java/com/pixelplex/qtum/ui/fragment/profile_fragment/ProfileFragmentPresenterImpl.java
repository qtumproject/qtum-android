package com.pixelplex.qtum.ui.fragment.profile_fragment;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.services.update_service.UpdateService;
import com.pixelplex.qtum.datastorage.listeners.LanguageChangeListener;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.about_fragment.AboutFragment;
import com.pixelplex.qtum.ui.fragment.language_fragment.LanguageFragment;
import com.pixelplex.qtum.ui.fragment.pin_fragment.PinFragment;
import com.pixelplex.qtum.ui.fragment.smart_contracts_fragment.SmartContractsFragment;
import com.pixelplex.qtum.ui.fragment.start_page_fragment.StartPageFragment;
import com.pixelplex.qtum.ui.fragment.subscribe_tokens_fragment.SubscribeTokensFragment;
import com.pixelplex.qtum.utils.ThemeUtils;
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
            settingsData.add(new SettingObject(R.string.language, R.drawable.ic_language, 0));
            settingsData.add(new SettingObject(R.string.change_pin, R.drawable.ic_changepin, 1));
            settingsData.add(new SettingObject(R.string.wallet_backup, R.drawable.ic_backup, 1));
            if(getView().getMainActivity().checkAvailabilityTouchId()) {
                settingsData.add(new SettingSwitchObject(R.string.touch_id, R.drawable.ic_touchid, 1, QtumSharedPreference.getInstance().isTouchIdEnable(getView().getContext())));
            }
            settingsData.add(new SettingObject(R.string.smart_contracts, R.drawable.ic_prof_smartcontr, 2));
            settingsData.add(new SettingObject(R.string.subscribe_tokens, R.drawable.ic_tokensubscribe, 2));
            settingsData.add(new SettingObject(R.string.about, R.drawable.ic_about, 4));
            settingsData.add(new SettingObject(R.string.switch_theme, R.drawable.ic_prof_theme, 4));
            settingsData.add(new SettingObject(R.string.log_out, R.drawable.ic_logout, 4));
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