package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;

import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


public interface ContractManagementFragmentView extends BaseFragmentView{
    void setRecyclerView(List<ContractMethod> contractMethodList);
    String getContractTemplateUiid();
}
