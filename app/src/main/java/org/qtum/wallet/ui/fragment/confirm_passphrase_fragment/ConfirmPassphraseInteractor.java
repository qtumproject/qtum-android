package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;


import rx.Observable;

public interface ConfirmPassphraseInteractor {
    Observable<String> createWallet(String passphrase);
    void setKeyGeneratedInstance(boolean isKeyGenerated);
}
