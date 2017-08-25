package com.pixelplex.qtum.ui.fragment.currency_fragment;


import com.pixelplex.qtum.model.Currency;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

interface CurrencyFragmentView extends BaseFragmentView{
    void setCurrencyList(List<Currency> currencyList);
}
