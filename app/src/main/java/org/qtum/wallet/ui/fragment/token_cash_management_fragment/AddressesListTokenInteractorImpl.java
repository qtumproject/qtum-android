package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.content.Context;
import android.text.TextUtils;

import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.wallet.datastorage.KeyStorage;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public class AddressesListTokenInteractorImpl implements AddressesListTokenInteractor {

    private WeakReference<Context> mContext;

    public AddressesListTokenInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public boolean isCurrencyValid(String currency) {
        return !TextUtils.isEmpty(currency);
    }

    @Override
    public boolean isAmountValid(String amountString) {
        return !TextUtils.isEmpty(amountString);
    }

    @Override
    public List<DeterministicKey> getKeys(int i) {
        return KeyStorage.getInstance().getKeyList(10);
    }
}
