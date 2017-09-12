package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface SubscribeTokensFragmentView extends BaseFragmentView {
    void setTokenList(List<Token> tokenList);
    List<Token> getTokenList();
}
