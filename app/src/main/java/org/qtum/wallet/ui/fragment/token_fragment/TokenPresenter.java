package org.qtum.wallet.ui.fragment.token_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public interface TokenPresenter extends BaseFragmentPresenter {
    Token getToken();

    void setToken(Token token);

    String getAbi();

    void onDecimalsPropertySuccess(String value);

    String onTotalSupplyPropertySuccess(Token token, String value);

}
