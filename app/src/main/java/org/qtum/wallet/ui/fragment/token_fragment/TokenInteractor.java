package org.qtum.wallet.ui.fragment.token_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.utils.ContractManagementHelper;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public interface TokenInteractor {
    String getCurrentAddress();

    String readAbiContract(String uiid);

    void setupPropertyTotalSupplyValue(Token token, ContractManagementHelper.GetPropertyValueCallBack totalSupplyValueCallback);

    void setupPropertySymbolValue(Token token, ContractManagementHelper.GetPropertyValueCallBack symbolValueCallback);

    void setupPropertyNameValue(Token token, ContractManagementHelper.GetPropertyValueCallBack nameValueCallback);

    void setupPropertyDecimalsValue(Token token, ContractManagementHelper.GetPropertyValueCallBack decimalsValueCallback);

    Token setTokenDecimals(Token token, String value);

    String handleTotalSupplyValue(Token token, String value);
}
