package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


public interface PinFragmentView extends BaseFragmentView {
    void confirm();
    void confirmError(String errorText);
}
