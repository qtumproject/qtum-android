package org.qtum.wallet.ui.fragment.currency_fragment;

import org.qtum.wallet.model.Currency;

import java.util.List;

public interface CurrencyInteractor {
    List<Currency> getCurrencies();
}
