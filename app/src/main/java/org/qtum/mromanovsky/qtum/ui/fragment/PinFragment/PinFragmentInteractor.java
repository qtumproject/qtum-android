package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragmentInteractorImpl;

public interface PinFragmentInteractor {
    int getPassword();
    void savePassword(int password);
    void registerKey(String key, String identifier, PinFragmentInteractorImpl.RegisterKeyCallBack callBack);
}
