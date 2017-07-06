package com.pixelplex.qtum.ui.fragment.ProfileFragment;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.dataprovider.listeners.LanguageChangeListener;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.activity.BaseActivity.BaseActivity;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.LanguageFragment.LanguageFragment;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;
import com.pixelplex.qtum.ui.fragment.SmartContractsFragment.SmartContractsFragment;
import com.pixelplex.qtum.ui.fragment.StartPageFragment.StartPageFragment;
import com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment.SubscribeTokensFragment;
import com.pixelplex.qtum.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    private UpdateService mUpdateService;

    private ProfileFragmentView mProfileFragmentView;
    private ProfileFragmentInteractorImpl mProfileFragmentInteractor;
    private LanguageChangeListener mLanguageChangeListener;

    List<SettingObject> settingsData;

    public ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView) {
        mProfileFragmentView = profileFragmentView;
        mProfileFragmentInteractor = new ProfileFragmentInteractorImpl(getView().getContext());
        initSettingsData();
    }

    private void initSettingsData() {
        settingsData = new ArrayList<>();
        settingsData.add(new SettingObject(R.string.language, R.drawable.ic_language, 0));
        settingsData.add(new SettingObject(R.string.change_pin, R.drawable.ic_changepin, 1));
        settingsData.add(new SettingObject(R.string.wallet_back_up, R.drawable.ic_backup, 1));
        if(getView().getMainActivity().checkTouchId()) {
            settingsData.add(new SettingSwitchObject(R.string.touch_id, R.drawable.ic_touchid, 1, QtumSharedPreference.getInstance().isTouchIdEnable(getView().getContext())));
        }
        settingsData.add(new SettingObject(R.string.subscribe_tokens,R.drawable.ic_tokensubscribe,2));
        settingsData.add(new SettingObject(R.string.smart_contracts,R.drawable.ic_token,2));
        settingsData.add(new SettingObject(R.string.about,R.drawable.ic_about,3));
        settingsData.add(new SettingObject(R.string.log_out, R.drawable.ic_logout, 3));
        settingsData.add(new SettingObject(R.string.switch_theme, R.drawable.ic_changepin, 4));
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
        getView().getMainActivity().setAuthenticationFlag(false);
        mUpdateService = getView().getMainActivity().getUpdateService();
        mUpdateService.stopMonitoring();

        StartPageFragment startPageFragment = StartPageFragment.newInstance(false);
        getView().getMainActivity().openRootFragment(startPageFragment);
        getView().getMainActivity().setIconChecked(0);
    }

    public void onLanguageClick(){
        LanguageFragment languageFragment = LanguageFragment.newInstance();
        getView().openFragment(languageFragment);
    }

    @Override
    public void onSmartContractsClick() {
        SmartContractsFragment smartContractsFragment = SmartContractsFragment.newInstance();
        getView().openFragment(smartContractsFragment);
    }

    @Override
    public void onSubscribeTokensClick() {
        SubscribeTokensFragment subscribeTokensFragment = SubscribeTokensFragment.newInstance();
        getView().openFragment(subscribeTokensFragment);
    }

    public void onTouchIdSwitched(boolean isChecked){
        QtumSharedPreference.getInstance().saveTouchIdEnable(getView().getContext(),isChecked);
    }

    public void switchTheme() {
        ThemeUtils.switchPreferencesTheme(getView().getContext());
        getView().getMainActivity().reloadActivity();
    }
}