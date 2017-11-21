package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import java.util.List;

public interface AddressesListTokenPresenter extends BaseFragmentPresenter {
    void saveTokens(List<Token> tokens);
}
