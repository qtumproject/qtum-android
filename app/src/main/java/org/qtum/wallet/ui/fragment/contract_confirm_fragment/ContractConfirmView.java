package org.qtum.wallet.ui.fragment.contract_confirm_fragment;

import org.qtum.wallet.QtumApplication;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


public interface ContractConfirmView extends BaseFragmentView {

    void makeToast(String s);

    QtumApplication getApplication();

    String getContractName();

    void updateFee(double minFee, double maxFee);
    void updateGasPrice(int minGasPrice, int maxGasPrice);
    void updateGasLimit(int minGasLimit, int maxGasLimit);
    void closeFragments();

}
