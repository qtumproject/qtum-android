package com.pixelplex.qtum.ui.fragment.pin_fragment;


import android.content.Context;

interface PinFragmentInteractor {
    String getPassword();
    void savePassword(String password);
    void saveSaltPassphrase(byte[] saltPassphrase);
    byte[] getSaltPassphrase();
    String getTouchIdPassword();
    void saveTouchIdPassword(String password);
    void loadWalletFromFile(PinFragmentInteractorImpl.LoadWalletFromFileCallBack callBack);
    void createWallet(Context context, PinFragmentInteractorImpl.CreateWalletCallBack callBack);
    void setKeyGeneratedInstance(boolean isKeyGenerated);
}
