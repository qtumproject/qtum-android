package com.pixelplex.qtum.ui.fragment.contract_confirm_fragment;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;


public interface ContractConfirmView extends BaseFragmentView {

    void makeToast(String s);

    QtumApplication getApplication();

    String getContractName();

}
