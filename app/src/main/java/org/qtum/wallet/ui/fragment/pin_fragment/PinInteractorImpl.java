package org.qtum.wallet.ui.fragment.pin_fragment;

import android.content.Context;

import org.bitcoinj.wallet.Wallet;
import org.qtum.wallet.utils.crypto.KeyStoreHelper;

import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSharedPreference;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import rx.Observable;


class PinInteractorImpl implements PinInteractor {

    private Context mContext;
    private final String QTUM_PIN_ALIAS = "qtum_alias";

    PinInteractorImpl(Context context) {
        mContext = context;
        try {
            KeyStoreHelper.createKeys(mContext,QTUM_PIN_ALIAS);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPassword() {
        String encryptedPinHash = QtumSharedPreference.getInstance().getWalletPassword(mContext);
        return KeyStoreHelper.decrypt(QTUM_PIN_ALIAS, encryptedPinHash);
    }

    @Override
    public void savePassword(String password) {
        String encryptedPinHash = KeyStoreHelper.encrypt(QTUM_PIN_ALIAS,password);
        QtumSharedPreference.getInstance().saveWalletPassword(mContext, encryptedPinHash);
    }

    @Override
    public void saveSaltPassphrase(byte[] saltPassphrase) {
        String encryptedSaltPassphrase = KeyStoreHelper.encryptBytes(QTUM_PIN_ALIAS,saltPassphrase);
        QtumSharedPreference.getInstance().saveSeed(mContext, encryptedSaltPassphrase);
    }

    @Override
    public byte[] getSaltPassphrase() {
        String encryptedSaltPassphrase = QtumSharedPreference.getInstance().getSeed(mContext);
        return KeyStoreHelper.decryptToBytes(QTUM_PIN_ALIAS,encryptedSaltPassphrase);
    }

    @Override
    public String getTouchIdPassword() {
        return QtumSharedPreference.getInstance().getTouchIdPassword(mContext);
    }

    @Override
    public void saveTouchIdPassword(String password) {
        QtumSharedPreference.getInstance().saveTouchIdPassword(mContext, password);
    }


    @Override
    public Observable<Wallet> loadWalletFromFile() {
        return KeyStorage.getInstance().loadWalletFromFile(mContext);
    }

    @Override
    public Observable<String> createWallet() {
        return KeyStorage.getInstance().createWallet(mContext);
    }

    @Override
    public void setKeyGeneratedInstance(boolean isKeyGenerated) {
        QtumSharedPreference.getInstance().setKeyGeneratedInstance(mContext, isKeyGenerated);
    }

}