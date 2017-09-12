package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;


import org.qtum.wallet.model.DeterministicKeyWithBalance;

public interface OnAddressClickListener {
    void onItemClick(DeterministicKeyWithBalance deterministicKeyWithBalance);
}
