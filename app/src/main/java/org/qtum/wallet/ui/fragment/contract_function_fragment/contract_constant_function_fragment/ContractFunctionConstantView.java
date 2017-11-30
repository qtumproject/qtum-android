package org.qtum.wallet.ui.fragment.contract_function_fragment.contract_constant_function_fragment;

import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface ContractFunctionConstantView extends BaseFragmentView {
    void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList);

    String getContractTemplateUiid();

    String getMethodName();

    void updateQueryResult(String queryResult);

}
