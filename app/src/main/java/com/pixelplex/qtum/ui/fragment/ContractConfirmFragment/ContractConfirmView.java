package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import com.pixelplex.qtum.QtumApplication;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public interface ContractConfirmView extends BaseFragmentView {

    void makeToast(String s);

    QtumApplication getApplication();

}
