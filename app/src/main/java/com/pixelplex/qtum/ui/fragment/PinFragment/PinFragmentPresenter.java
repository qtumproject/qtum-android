package com.pixelplex.qtum.ui.fragment.PinFragment;


interface PinFragmentPresenter {
    void confirm(String password);
    void cancel();
    void setAction(String action);
}
