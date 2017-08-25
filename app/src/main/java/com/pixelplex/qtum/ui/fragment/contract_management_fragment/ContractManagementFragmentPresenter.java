package com.pixelplex.qtum.ui.fragment.contract_management_fragment;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;


public class ContractManagementFragmentPresenter extends BaseFragmentPresenterImpl {

    private ContractManagementFragmentView mContractManagementFragmentView;

    ContractManagementFragmentPresenter(ContractManagementFragmentView contractManagementFragmentView){
        mContractManagementFragmentView = contractManagementFragmentView;
    }

    public void getAbiFromFile(){
        List<ContractMethod> contractMethodList = FileStorageManager.getInstance().getContractMethods(getView().getContext(),getView().getContractTemplateUiid());
        if(contractMethodList != null) {
            getView().setRecyclerView(contractMethodList, true);
        } else {
            getView().setAlertDialog(getView().getContext().getString(R.string.error),getView().getContext().getString(R.string.fail_to_get_contract_methods), BaseFragment.PopUpType.error);
        }
    }

    public void getAbiFromString(String abi){
        List<ContractMethod> contractMethodList = FileStorageManager.getInstance().getContractMethodsByAbiString(getView().getContext(), abi);
        if(contractMethodList != null) {
            getView().setRecyclerView(contractMethodList, false);
        } else {
            getView().setAlertDialog(getView().getContext().getString(R.string.error),getView().getContext().getString(R.string.fail_to_get_contract_methods), BaseFragment.PopUpType.error);
        }
    }


    @Override
    public ContractManagementFragmentView getView() {
        return mContractManagementFragmentView;
    }

}
