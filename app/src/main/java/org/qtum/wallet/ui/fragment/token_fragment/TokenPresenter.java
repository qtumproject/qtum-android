package org.qtum.wallet.ui.fragment.token_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface TokenPresenter extends BaseFragmentPresenter {
    Token getToken();

    void setToken(Token token);

    String getAbi();

    void onDecimalsPropertySuccess(String value);

    String onTotalSupplyPropertySuccess(Token token, String value);
}
