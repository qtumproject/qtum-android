package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import android.content.Context;

interface PinFragmentInteractor {
    int getPassword();
    void savePassword(int password);
    void loadWalletFromFile(PinFragmentInteractorImpl.LoadWalletFromFileCallBack callBack);
    void createWallet(Context context, PinFragmentInteractorImpl.CreateWalletCallBack callBack);
    void setKeyGeneratedInstance(boolean isKeyGenerated);
}
