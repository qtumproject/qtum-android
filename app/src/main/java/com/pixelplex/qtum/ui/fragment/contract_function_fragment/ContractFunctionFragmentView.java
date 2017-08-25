package com.pixelplex.qtum.ui.fragment.contract_function_fragment;

import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface ContractFunctionFragmentView extends BaseFragmentView{
    void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList);
    String getContractTemplateUiid();
    String getMethodName();
}
