package com.pixelplex.qtum.ui.fragment.other_tokens;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface OtherTokensView extends BaseFragmentView {
    void setTokensData(List<Token> tokensData);
    void updateTokensData(List<Token> tokensData);
}
