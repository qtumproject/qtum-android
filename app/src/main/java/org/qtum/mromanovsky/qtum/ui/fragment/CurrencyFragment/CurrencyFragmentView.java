package org.qtum.mromanovsky.qtum.ui.fragment.CurrencyFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface CurrencyFragmentView extends BaseFragmentView{
    void setTokenList(List<String> tokenList);
}
