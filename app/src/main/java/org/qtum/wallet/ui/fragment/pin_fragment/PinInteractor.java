package org.qtum.wallet.ui.fragment.pin_fragment;


import android.content.Context;

public interface PinInteractor {
    String getPassword();
    void savePassword(String password);
    void saveSaltPassphrase(byte[] saltPassphrase);
    byte[] getSaltPassphrase();
    String getTouchIdPassword();
    void saveTouchIdPassword(String password);
    void loadWalletFromFile(PinInteractorImpl.LoadWalletFromFileCallBack callBack);
    void createWallet(Context context, PinInteractorImpl.CreateWalletCallBack callBack);
    void setKeyGeneratedInstance(boolean isKeyGenerated);
}
