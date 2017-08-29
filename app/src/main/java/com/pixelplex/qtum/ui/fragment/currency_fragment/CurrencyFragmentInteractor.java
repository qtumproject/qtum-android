package com.pixelplex.qtum.ui.fragment.currency_fragment;


import com.pixelplex.qtum.model.contract.Token;

import java.util.List;

interface CurrencyFragmentInteractor {
    List<Token> getTokenList();
}
