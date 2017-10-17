package org.qtum.wallet.ui.fragment.currency_fragment;


import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.contract.Token;

import java.util.List;

public interface CurrencyInteractor {
    List<Currency> getCurrencies();
}
