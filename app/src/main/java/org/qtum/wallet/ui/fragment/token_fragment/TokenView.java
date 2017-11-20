package org.qtum.wallet.ui.fragment.token_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;
import org.qtum.wallet.utils.ContractManagementHelper;

public interface TokenView extends BaseFragmentView {
    void setBalance(String balance);

    void setTokenAddress(String address);

    void setQtumAddress(String address);

    void onContractPropertyUpdated(String propName, String propValue);

    void setSenderAddress(String address);

    String getCurrency();

    String getTokenBalance();

    boolean isAbiEmpty(String abi);

    ContractManagementHelper.GetPropertyValueCallBack getNameValueCallback();

    ContractManagementHelper.GetPropertyValueCallBack getSymbolValueCallback();

    ContractManagementHelper.GetPropertyValueCallBack getDecimalsValueCallback();

    ContractManagementHelper.GetPropertyValueCallBack getTotalSupplyValueCallback();
}
