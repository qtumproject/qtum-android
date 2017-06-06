package com.pixelplex.qtum.ui.fragment.ContractFunctionFragment;

import com.pixelplex.qtum.SmartContractsManager.StorageManager;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by max-v on 6/2/2017.
 */

public class ContractFunctionFragmentPresenter extends BaseFragmentPresenterImpl {

    ContractFunctionFragmentView mContractMethodFragmentView;

    ContractFunctionFragmentPresenter(ContractFunctionFragmentView contractMethodFragmentView){
        mContractMethodFragmentView = contractMethodFragmentView;
    }

    @Override
    public ContractFunctionFragmentView getView() {
        return mContractMethodFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<ContractMethod> list = StorageManager.getInstance().getContractMethods(getView().getContext(),getView().getContractTemplateName());
        for(ContractMethod contractMethod : list){
            if(contractMethod.name.equals(getView().getMethodName())){
                getView().setUpParameterList(contractMethod.inputParams);
                return;
            }
        }
    }
}
