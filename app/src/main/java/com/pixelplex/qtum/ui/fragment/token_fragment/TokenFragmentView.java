package com.pixelplex.qtum.ui.fragment.token_fragment;

import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;


public interface TokenFragmentView extends BaseFragmentView {
    void setBalance(String balance);
    void setTokenAddress(String address);
    void onContractPropertyUpdated(String propName, String propValue);
    void setSenderAddress(String address);
    String getCurrency();
    String getTokenBalance();
}
