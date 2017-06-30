package com.pixelplex.qtum.ui.fragment.PinFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface PinFragmentView extends BaseFragmentView {
    void confirmError(String errorText);
    void updateState(String state);
    void clearError();
    void setToolBarTitle(int titleID);
    void setPin(String pin);
}
