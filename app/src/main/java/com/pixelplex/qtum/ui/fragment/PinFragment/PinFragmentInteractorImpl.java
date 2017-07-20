package com.pixelplex.qtum.ui.fragment.PinFragment;

import android.content.Context;

import org.bitcoinj.wallet.Wallet;

import com.pixelplex.qtum.crypto.KeyStoreHelper;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class PinFragmentInteractorImpl implements PinFragmentInteractor {

    private Context mContext;
    static boolean isDataLoaded = false;
    static String sPassphrase;
    private final String QTUM_PIN_ALIAS = "qtum_alias";

    PinFragmentInteractorImpl(Context context) {
        mContext = context;
        try {
            KeyStoreHelper.createKeys(mContext,QTUM_PIN_ALIAS);
//            KeyStoreHelper.createKeys(mContext,QTUM_SALT_PASSPHRASE_ALIAS);
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
    public String getTouchIdPassword() {
        return QtumSharedPreference.getInstance().getTouchIdPassword(mContext);
    }

    @Override
    public void saveTouchIdPassword(String password) {
        QtumSharedPreference.getInstance().saveTouchIdPassword(mContext, password);
    }


    @Override
    public void loadWalletFromFile(final LoadWalletFromFileCallBack callBack) {
        KeyStorage.getInstance()
                .loadWalletFromFile(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Wallet>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Wallet wallet) {
                        isDataLoaded = true;
                        callBack.onSuccess();
                    }
                });
    }

    @Override
    public void createWallet(Context context, final CreateWalletCallBack callBack) {
        KeyStorage.getInstance()
                .createWallet(mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String passphrase) {
                        setKeyGeneratedInstance(true);
                        isDataLoaded = true;
                        sPassphrase = passphrase;
                        callBack.onSuccess();
                    }
                });
    }

    @Override
    public void setKeyGeneratedInstance(boolean isKeyGenerated) {
        QtumSharedPreference.getInstance().setKeyGeneratedInstance(mContext, isKeyGenerated);
    }


    interface CreateWalletCallBack {
        void onSuccess();
    }

    interface LoadWalletFromFileCallBack {
        void onSuccess();
    }

}
