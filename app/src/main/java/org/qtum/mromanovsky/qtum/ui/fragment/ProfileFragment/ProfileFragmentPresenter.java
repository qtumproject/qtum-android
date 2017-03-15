package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;


interface ProfileFragmentPresenter {
    void changePin();
    void logOut();
    void walletBackUp();
    void onLogOutYesClick();
    void onCreateTokenClick();
    void onSubscribeTokensClick();
}
