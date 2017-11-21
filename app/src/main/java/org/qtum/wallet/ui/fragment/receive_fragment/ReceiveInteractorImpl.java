package org.qtum.wallet.ui.fragment.receive_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.KeyStorage;

import java.math.BigDecimal;

class ReceiveInteractorImpl implements ReceiveInteractor {
    private Context mContext;

    ReceiveInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public String getCurrentReceiveAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }

    @Override
    public String formatReceiveAddress(String addr) {
        return String.format("qtum:%s?", addr);
    }

    @Override
    public String formatAmount(String amount) {
        return String.format("amount=%s&", amount);
    }

    @Override
    public String formatTokenAddress(String addr) {
        return String.format("&tokenAddress=%s", addr);
    }

    @Override
    public String formatBalance(String balanceString) {
        return String.format("%S QTUM", balanceString);
    }

    @Override
    public String formatUnconfirmedBalance(BigDecimal unconfirmedBalance) {
        return String.format("%S QTUM", String.valueOf(unconfirmedBalance.floatValue()));
    }
}