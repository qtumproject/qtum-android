package com.pixelplex.qtum.ui.fragment.ContractFunctionFragment;

import com.pixelplex.qtum.SmartContractsManager.StorageManager;
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
    public BaseFragmentView getView() {
        return mContractMethodFragmentView;
    }

    public List<ContractMethodParameter> getConstructorByName(String name) {
        return StorageManager.getInstance().getContractConstructor(getView().getContext(),name).inputParams;
    }

}
