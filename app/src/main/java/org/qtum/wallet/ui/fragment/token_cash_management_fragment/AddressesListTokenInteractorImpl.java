package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.content.Context;
import android.text.TextUtils;

import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;

import java.lang.ref.WeakReference;
import java.util.List;


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
        return KeyStorage.getInstance().getKeyList();
    }

    @Override
    public boolean isValidForAddress(TokenBalance tokenBalance, DeterministicKeyWithTokenBalance keyWithTokenBalanceFrom) {
        return tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()) != null;
    }

    @Override
    public boolean isValidBalance(TokenBalance tokenBalance, DeterministicKeyWithTokenBalance keyWithTokenBalanceFrom, String amountString) {
        return tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()).getBalance().floatValue() > Float.valueOf(amountString);
    }
}
