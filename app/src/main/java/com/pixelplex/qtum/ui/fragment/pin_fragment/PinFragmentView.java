package com.pixelplex.qtum.ui.fragment.pin_fragment;

import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;


interface PinFragmentView extends BaseFragmentView {
    void confirmError(String errorText);
    void updateState(String state);
    void clearError();
    void setToolBarTitle(int titleID);
    void setPin(String pin);
    String getPassphrase();
}
