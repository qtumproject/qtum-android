package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;

public interface OnAddressTokenClickListener {
    void onItemClick(DeterministicKeyWithTokenBalance item);
}
