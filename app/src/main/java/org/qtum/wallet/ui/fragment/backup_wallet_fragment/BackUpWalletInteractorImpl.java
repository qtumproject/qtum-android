package org.qtum.wallet.ui.fragment.backup_wallet_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.utils.crypto.AESUtil;
import org.qtum.wallet.utils.crypto.KeyStoreHelper;


class BackUpWalletInteractorImpl implements BackUpWalletInteractor {

    private Context mContext;

    private final String QTUM_PIN_ALIAS = "qtum_alias";

    BackUpWalletInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public String getPassphrase(String pin) {
        String cryptoSaltPassphrase = QtumSharedPreference.getInstance().getSeed(mContext);
        byte[] saltPassphrase = KeyStoreHelper.decryptToBytes(QTUM_PIN_ALIAS,cryptoSaltPassphrase);
        return AESUtil.decryptBytes(pin,saltPassphrase);
    }
}
