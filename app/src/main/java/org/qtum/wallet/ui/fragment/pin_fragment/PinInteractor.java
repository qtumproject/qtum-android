package org.qtum.wallet.ui.fragment.pin_fragment;


import android.content.Context;

import org.bitcoinj.wallet.Wallet;

import rx.Observable;

public interface PinInteractor {
    String getPassword();
    void savePassword(String password);
    void saveSaltPassphrase(byte[] saltPassphrase);
    byte[] getSaltPassphrase();
    String getTouchIdPassword();
    void saveTouchIdPassword(String password);
    Observable<Wallet> loadWalletFromFile();
    Observable<String> createWallet();
    void setKeyGeneratedInstance(boolean isKeyGenerated);
}
