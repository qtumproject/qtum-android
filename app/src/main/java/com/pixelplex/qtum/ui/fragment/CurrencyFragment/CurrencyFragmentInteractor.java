package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Token;

import java.util.List;

interface CurrencyFragmentInteractor {
    List<Token> getTokenList();
}
