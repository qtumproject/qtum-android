package com.pixelplex.qtum.ui.fragment.TokenFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

/**
 * Created by kirillvolkov on 01.06.17.
 */

public interface TokenFragmentView extends BaseFragmentView {
    void setBalance(float balance);
    void setTokenAddress(String address);
    void onContractPropertyUpdated(String propName, String propValue);
    void setSenderAddress(String address);
}
