package com.pixelplex.qtum.ui.fragment.pin_fragment;


interface PinFragmentPresenter {
    void confirm(String password);
    void cancel();
    void setAction(String action);
}
