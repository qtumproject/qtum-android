package org.qtum.wallet.ui.fragment.contract_management_fragment;

import android.text.TextUtils;

import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;


public class ContractManagementPresenterImpl extends BaseFragmentPresenterImpl implements ContractManagementPresenter{

    private ContractManagementView mContractManagementFragmentView;
    private ContractManagementInteractor mContractManagementInteractor;

    ContractManagementPresenterImpl(ContractManagementView contractManagementFragmentView, ContractManagementInteractor contractManagementInteractor){
        mContractManagementFragmentView = contractManagementFragmentView;
        mContractManagementInteractor = contractManagementInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        if(!TextUtils.isEmpty(getView().getContractAddress())){
            getAbiFromFile();
        } else {
            getView().setTitleText(R.string.contract_details);
            getAbiFromString(getView().getContractABI());
        }
    }

    public void getAbiFromFile(){
        List<ContractMethod> contractMethodList = FileStorageManager.getInstance().getContractMethods(getView().getContext(),getView().getContractTemplateUiid());
        if(contractMethodList != null) {
            getView().setRecyclerView(contractMethodList, true);
        } else {
            getView().setAlertDialog(R.string.error,R.string.fail_to_get_contract_methods, BaseFragment.PopUpType.error);
        }
    }

    public void getAbiFromString(String abi){
        List<ContractMethod> contractMethodList = FileStorageManager.getInstance().getContractMethodsByAbiString(getView().getContext(), abi);
        if(contractMethodList != null) {
            getView().setRecyclerView(contractMethodList, false);
        } else {
            getView().setAlertDialog(R.string.error,R.string.fail_to_get_contract_methods, BaseFragment.PopUpType.error);
        }
    }


    @Override
    public ContractManagementView getView() {
        return mContractManagementFragmentView;
    }

}
