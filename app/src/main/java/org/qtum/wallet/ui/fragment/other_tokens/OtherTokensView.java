package org.qtum.wallet.ui.fragment.other_tokens;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface OtherTokensView extends BaseFragmentView {
    void setTokensData(List<Token> tokensData);
    void updateTokensData(List<Token> tokensData);
}
