package com.pixelplex.qtum.ui.fragment.subscribe_tokens_fragment;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface SubscribeTokensFragmentView extends BaseFragmentView {
    void setTokenList(List<Token> tokenList);
    List<Token> getTokenList();
}
