package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


public interface PinFragmentView extends BaseFragmentView {
    void confirmError(String errorText);
    void setDialogProgressBar();
    void dismissDialogProgressBar();
    void updateState();
    void clearError();
}
