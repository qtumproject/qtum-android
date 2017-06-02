package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

/**
 * Created by max-v on 6/2/2017.
 */

public class ContractManagementFragmentPresenter extends BaseFragmentPresenterImpl {

    ContractManagementFragmentView mContractManagementFragmentView;

    ContractManagementFragmentPresenter(ContractManagementFragmentView contractManagementFragmentView){
        mContractManagementFragmentView = contractManagementFragmentView;
    }

    @Override
    public BaseFragmentView getView() {
        return mContractManagementFragmentView;
    }
}
