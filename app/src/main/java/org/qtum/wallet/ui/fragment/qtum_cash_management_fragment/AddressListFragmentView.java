package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import org.qtum.wallet.model.DeterministicKeyWithBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface AddressListFragmentView extends BaseFragmentView {
    void updateAddressList(List<DeterministicKeyWithBalance> deterministicKeyWithBalance);
}
