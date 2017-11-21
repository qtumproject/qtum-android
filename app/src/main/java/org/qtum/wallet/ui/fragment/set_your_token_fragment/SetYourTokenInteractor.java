package org.qtum.wallet.ui.fragment.set_your_token_fragment;

import org.qtum.wallet.model.contract.ContractMethod;

public interface SetYourTokenInteractor {
    ContractMethod getContractConstructor(String uiid);
}
