package org.qtum.wallet.ui.fragment.pin_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


interface PinFragmentView extends BaseFragmentView {
    void confirmError(String errorText);
    void updateState(String state);
    void clearError();
    void setToolBarTitle(int titleID);
    void setPin(String pin);
    String getPassphrase();
}
