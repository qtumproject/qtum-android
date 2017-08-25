package com.pixelplex.qtum.ui.fragment.contract_management_fragment;

import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface ContractManagementFragmentView extends BaseFragmentView{
    void setRecyclerView(List<ContractMethod> contractMethodList, boolean needToGetValue);
    String getContractTemplateUiid();
}
