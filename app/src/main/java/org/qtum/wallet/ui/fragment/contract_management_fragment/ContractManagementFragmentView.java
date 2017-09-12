package org.qtum.wallet.ui.fragment.contract_management_fragment;

import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface ContractManagementFragmentView extends BaseFragmentView {
    void setRecyclerView(List<ContractMethod> contractMethodList, boolean needToGetValue);
    String getContractTemplateUiid();
}
