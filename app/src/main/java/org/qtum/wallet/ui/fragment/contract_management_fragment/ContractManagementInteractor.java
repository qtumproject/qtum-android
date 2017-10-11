package org.qtum.wallet.ui.fragment.contract_management_fragment;


import org.qtum.wallet.model.contract.ContractMethod;

import java.util.List;

public interface ContractManagementInteractor {
    List<ContractMethod> getContractListByUiid(String uiid);
    List<ContractMethod> getContractListByAbi(String abi);
}
