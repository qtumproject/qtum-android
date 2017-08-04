package com.pixelplex.qtum.ui.fragment.AddressesListFragmentToken;

import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public interface OnAddressTokenClickListener {
    void onItemClick(DeterministicKeyWithTokenBalance item);
}
