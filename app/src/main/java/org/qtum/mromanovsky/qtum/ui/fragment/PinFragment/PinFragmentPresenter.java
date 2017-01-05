package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenter;


/**
 * Created by max-v on 1/5/2017.
 */

public interface PinFragmentPresenter extends BaseFragmentPresenter {
    void confirm(String password, boolean isCreating);
}
