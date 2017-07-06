package com.pixelplex.qtum.ui.fragment.ContractFunctionFragment;

import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


public interface ContractFunctionFragmentView extends BaseFragmentView{
    void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList);
    long getContractTemplateUiid();
    String getMethodName();
}
