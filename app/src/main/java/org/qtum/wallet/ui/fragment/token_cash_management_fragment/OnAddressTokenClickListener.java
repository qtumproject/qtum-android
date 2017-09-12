package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public interface OnAddressTokenClickListener {
    void onItemClick(DeterministicKeyWithTokenBalance item);
}
