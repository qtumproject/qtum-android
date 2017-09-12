package org.qtum.wallet.ui.fragment.contract_management_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

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
