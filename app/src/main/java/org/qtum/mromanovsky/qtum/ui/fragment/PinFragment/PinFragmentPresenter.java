package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


public interface PinFragmentPresenter{
    void confirm(String password, boolean isCreating);
    void cancel();
}
