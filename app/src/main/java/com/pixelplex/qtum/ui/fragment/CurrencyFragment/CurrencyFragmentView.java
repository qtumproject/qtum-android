package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface CurrencyFragmentView extends BaseFragmentView{
    void setTokenList(List<String> tokenList);
}
