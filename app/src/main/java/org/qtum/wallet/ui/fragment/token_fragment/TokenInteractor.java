package org.qtum.wallet.ui.fragment.token_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.utils.ContractManagementHelper;

import rx.Subscriber;

public interface TokenInteractor {
    String getCurrentAddress();

    String readAbiContract(String uiid);

    void setupPropertyTotalSupplyValue(Token token, Subscriber<String> stringSubscriber);

    void setupPropertySymbolValue(Token token, Subscriber<String> stringSubscriber);

    void setupPropertyNameValue(Token token, Subscriber<String> stringSubscriber);

    void setupPropertyDecimalsValue(Token token, Subscriber<String> stringSubscriber);

    Token setTokenDecimals(Token token, String value);

    String handleTotalSupplyValue(Token token, String value);
}
