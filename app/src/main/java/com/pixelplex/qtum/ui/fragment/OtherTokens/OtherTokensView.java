package com.pixelplex.qtum.ui.fragment.OtherTokens;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


interface OtherTokensView extends BaseFragmentView {
    void setTokensData(List<Token> tokensData);
    void updateTokensData(List<Token> tokensData);
}
