package com.pixelplex.qtum.ui.fragment.PinFragment;


import android.content.Context;

interface PinFragmentInteractor {
    String getPassword();
    void savePassword(String password);
    String getTouchIdPassword();
    void saveTouchIdPassword(String password);
    void loadWalletFromFile(PinFragmentInteractorImpl.LoadWalletFromFileCallBack callBack);
    void createWallet(Context context, PinFragmentInteractorImpl.CreateWalletCallBack callBack);
    void setKeyGeneratedInstance(boolean isKeyGenerated);
}
