package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface PinFragmentView extends BaseFragmentView {
    void confirmError(String errorText);
    void updateState();
    void clearError();
}
