package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;

import com.pixelplex.qtum.SmartContractsManager.StorageManager;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by max-v on 6/2/2017.
 */

public class ContractManagementFragmentPresenter extends BaseFragmentPresenterImpl {

    ContractManagementFragmentView mContractManagementFragmentView;

    ContractManagementFragmentPresenter(ContractManagementFragmentView contractManagementFragmentView){
        mContractManagementFragmentView = contractManagementFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<ContractMethod> contractMethodList = StorageManager.getInstance().getContractMethods(getView().getContext(),getView().getContractTemplateName());
        getView().setRecyclerView(contractMethodList);
    }

    @Override
    public ContractManagementFragmentView getView() {
        return mContractManagementFragmentView;
    }
}
