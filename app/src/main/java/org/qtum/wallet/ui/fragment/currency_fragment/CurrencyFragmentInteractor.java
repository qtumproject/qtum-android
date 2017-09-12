package org.qtum.wallet.ui.fragment.currency_fragment;


import org.qtum.wallet.model.contract.Token;

import java.util.List;

interface CurrencyFragmentInteractor {
    List<Token> getTokenList();
}
