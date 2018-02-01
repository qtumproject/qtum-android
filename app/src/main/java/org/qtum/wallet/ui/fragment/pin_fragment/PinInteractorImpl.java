package org.qtum.wallet.ui.fragment.pin_fragment;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import org.qtum.wallet.R;
import org.qtum.wallet.utils.CryptoUtils;
import org.qtum.wallet.utils.CryptoUtilsCompat;
import org.qtum.wallet.utils.crypto.AESUtil;
import org.qtum.wallet.utils.crypto.KeyStoreHelper;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSharedPreference;

import javax.crypto.Cipher;

import rx.Observable;

import static org.qtum.wallet.utils.crypto.KeyStoreHelper.trimEndSpaces;

class PinInteractorImpl implements PinInteractor {

    private Context mContext;
    private final String QTUM_PIN_ALIAS = "qtum_alias";

    PinInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public String getPassword() {
        String sixDigitPassword = getSixDigitPassword();
        if (!TextUtils.isEmpty(sixDigitPassword)) {
            return trimEndSpaces(sixDigitPassword);
        } else {
            return trimEndSpaces(getFourDigitPassword());
        }
        //delete 4 spaces because after API 18 android have bug
        //(https://stackoverflow.com/questions/34472004/android-preferences-adding-unwanted-chars)
    }

    @Override
    public void savePassword(String password) {
        saveSixDigitPassword(password);
    }


    private String getFourDigitPassword() {
        String encryptedPinHash = QtumSharedPreference.getInstance().getPassword(mContext);
        return KeyStoreHelper.decrypt(QTUM_PIN_ALIAS, encryptedPinHash);
    }

    @Override
    public String getSixDigitPassword() {
        return QtumSharedPreference.getInstance().getSixDigitPassword(mContext);
    }

    @Override
    public Integer getFailedAttemptsCount() {
        return QtumSharedPreference.getInstance().getFailedAttemptsCount(mContext);
    }

    @Override
    public Long getBanTime() {
        return QtumSharedPreference.getInstance().getBanTime(mContext);
    }

    @Override
    public void setFailedAttemptsCount(int failedAttemptsCount) {
        QtumSharedPreference.getInstance().setFailedAttemptsCount(mContext, failedAttemptsCount);
    }

    @Override
    public void setBanTime(long banTime) {
        QtumSharedPreference.getInstance().setBanTime(mContext, banTime);
    }

    @Override
    public void saveSixDigitPassword(String password) {
        QtumSharedPreference.getInstance().saveSixDigitPassword(mContext, password);
    }

    @Override
    public void savePassphraseSaltWithPin(String pin, String passphrase) {
        byte[] saltPassphrase = AESUtil.encryptToBytes(pin, passphrase);
        String encryptedSaltPassphrase = Base64.encodeToString(saltPassphrase, Base64.DEFAULT);
        QtumSharedPreference.getInstance().saveSeed(mContext, encryptedSaltPassphrase);
    }

    @Override
    public byte[] getSaltPassphrase() {
        String encryptedSaltPassphrase = QtumSharedPreference.getInstance().getSeed(mContext);
        return Base64.decode(encryptedSaltPassphrase, Base64.DEFAULT);
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
    public String getRandomSeed(){
        return KeyStorage.getInstance().getRandomSeed();
    }

    @Override
    public void setKeyGeneratedInstance(boolean isKeyGenerated) {
        QtumSharedPreference.getInstance().setKeyGeneratedInstance(mContext, isKeyGenerated);
    }

    @Override
    public String decode(String encoded, Cipher cipher) {
        return CryptoUtils.decode(encoded, cipher);
    }

    @Override
    public Observable<String> encodeInBackground(String pin) {
        return CryptoUtils.encodeInBackground(pin);
    }

    @Override
    public String generateSHA256String(String pin) {
        return CryptoUtilsCompat.generateSHA256String(pin);
    }

    @Override
    public String getUnSaltPassphrase(String oldPin) {
        byte[] oldSaltPassphrase = getSaltPassphrase();
        return AESUtil.decryptBytes(oldPin, oldSaltPassphrase);
    }

    @Override
    public String getBanPinString(int min) {
        return mContext.getString(R.string.sorry_please_try_again_in) + " " + min + " " + mContext.getString(R.string.minutes);
    }

    @Override
    public Observable<String> loadWallet(String code) {
        return KeyStorage.getInstance().createWallet(KeyStoreHelper.getSeed(mContext, code));
    }

}