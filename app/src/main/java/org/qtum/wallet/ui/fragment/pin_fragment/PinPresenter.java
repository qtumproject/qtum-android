package org.qtum.wallet.ui.fragment.pin_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import javax.crypto.Cipher;

interface PinPresenter extends BaseFragmentPresenter{
    void confirm(String password);
    void cancel();
    void setAction(String action);
    void onAuthenticationSucceeded(Cipher cipher);
}
