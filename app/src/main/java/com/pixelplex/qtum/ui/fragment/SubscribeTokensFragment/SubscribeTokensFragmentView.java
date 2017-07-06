package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


interface SubscribeTokensFragmentView extends BaseFragmentView {
    void setTokenList(List<Token> tokenList);
    List<Token> getTokenList();
}
