package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import org.qtum.wallet.model.AddressWithBalance;

public interface OnAddressClickListener {
    void onItemClick(AddressWithBalance deterministicKeyWithBalance);
}
