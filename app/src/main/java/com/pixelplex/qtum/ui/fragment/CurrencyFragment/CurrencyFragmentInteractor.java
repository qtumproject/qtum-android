package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import com.pixelplex.qtum.model.contract.Token;

import java.util.List;

interface CurrencyFragmentInteractor {
    List<Token> getTokenList();
}
