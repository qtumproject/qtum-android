package com.pixelplex.qtum.ui.fragment.AddressListFragment;


import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;

public interface OnAddressClickListener {
    void onItemClick(DeterministicKeyWithBalance deterministicKeyWithBalance);
}
