package org.qtum.wallet.ui.fragment.pin_fragment;

import javax.crypto.Cipher;

import rx.Observable;

public interface PinInteractor {
    String getPassword();

    void savePassword(String password);

    void savePassphraseSaltWithPin(String pin, String passphrase);

    byte[] getSaltPassphrase();

    String getTouchIdPassword();

    void saveTouchIdPassword(String password);

    Observable<String> createWallet();

    void setKeyGeneratedInstance(boolean isKeyGenerated);

    String decode(String encoded, Cipher cipher);

    Observable<String> encodeInBackground(String pin);

    String generateSHA256String(String pin);

    String getUnSaltPassphrase(String oldPin);

    void saveSixDigitPassword(String password);

    String getSixDigitPassword();

    Integer getFailedAttemptsCount();

    Long getBanTime();

    void setFailedAttemptsCount(int failedAttemptsCount);

    void setBanTime(long banTime);

    String getBanPinString(int min);

    Observable<String> loadWallet(String code);
}
