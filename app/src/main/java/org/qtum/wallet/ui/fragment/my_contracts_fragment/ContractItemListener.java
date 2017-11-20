package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.model.contract.Contract;

public interface ContractItemListener {
    void onUnsubscribeClick(Contract contract);
}
