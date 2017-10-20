package org.qtum.wallet.ui.fragment.profile_fragment;


import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import java.util.List;

public interface ProfilePresenter extends BaseFragmentPresenter {
    void onTouchIdSwitched(boolean isChecked);

    void clearWallet();

    List<SettingObject> getSettingsData();

    void setupLanguageChangeListener(LanguageChangeListener listener);

    void removeLanguageListener(LanguageChangeListener listener);

}
