package com.pixelplex.qtum.ui.fragment.addresses_fragment;


import org.bitcoinj.crypto.DeterministicKey;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

interface AddressesFragmentView extends BaseFragmentView{
    void updateAddressList(List<DeterministicKey> deterministicKeys);
    void setAdapterNull();
}
