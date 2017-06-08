package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;

import java.util.List;

interface CurrencyFragmentInteractor {
    List<ContractInfo> getTokenList();
}
