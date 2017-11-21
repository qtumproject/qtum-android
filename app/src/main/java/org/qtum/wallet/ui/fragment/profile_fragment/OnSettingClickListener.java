package org.qtum.wallet.ui.fragment.profile_fragment;

public interface OnSettingClickListener {
    void onSettingClick(int key);

    void onSwitchChange(int key, boolean isChecked);
}
