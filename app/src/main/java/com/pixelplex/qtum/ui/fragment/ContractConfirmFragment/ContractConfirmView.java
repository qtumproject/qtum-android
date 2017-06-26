package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


public interface ContractConfirmView extends BaseFragmentView {

    void makeToast(String s);

    QtumApplication getApplication();

}
