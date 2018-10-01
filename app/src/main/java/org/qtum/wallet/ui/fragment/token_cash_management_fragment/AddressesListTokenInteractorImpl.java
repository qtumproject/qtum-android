package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.content.Context;
import android.text.TextUtils;

import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.model.AddressWithTokenBalance;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
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
    public List<String> getAddresses() {
        return KeyStorage.getInstance().getAddresses();
    }

    @Override
    public boolean isValidForAddress(TokenBalance tokenBalance, AddressWithTokenBalance keyWithTokenBalanceFrom) {
        return tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()) != null;
    }

    @Override
    public boolean isValidBalance(TokenBalance tokenBalance, AddressWithTokenBalance keyWithTokenBalanceFrom, String amountString, Integer decimalUnits) {
        if(tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()).getBalance() != null
                && !tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()).getBalance().toString().equals("0")) {
            return (tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()).getBalance().divide(new BigDecimal(Math.pow(10, decimalUnits)), BigDecimal.ROUND_DOWN)).compareTo(new BigDecimal(amountString)) > 0;
        } else {
            return false;
        }
    }
}