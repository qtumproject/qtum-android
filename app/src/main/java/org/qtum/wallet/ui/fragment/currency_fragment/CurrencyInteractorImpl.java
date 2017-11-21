package org.qtum.wallet.ui.fragment.currency_fragment;

import android.content.Context;

import org.qtum.wallet.R;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.CurrencyToken;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.datastorage.TinyDB;

import java.util.ArrayList;
import java.util.List;

class CurrencyInteractorImpl implements CurrencyInteractor {

    private Context mContext;

    CurrencyInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        List<Token> tokens = (new TinyDB(mContext)).getTokenList();
        Currency currency = new Currency("Qtum " + mContext.getString(R.string.default_currency));
        currencies.add(currency);
        for (Token token : tokens) {
            if (token.isHasBeenCreated() && token.isSubscribe()) {
                currency = new CurrencyToken(token.getContractName(), token);
                currencies.add(currency);
            }
        }
        return currencies;
    }
}
