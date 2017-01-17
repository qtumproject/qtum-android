package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragmentInteractorImpl;

public interface PinFragmentPresenter {
    void confirm(String[] password, String action);
    void cancel(String action);
}
