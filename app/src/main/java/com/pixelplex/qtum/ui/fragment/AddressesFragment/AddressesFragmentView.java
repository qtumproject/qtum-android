package com.pixelplex.qtum.ui.fragment.AddressesFragment;


import org.bitcoinj.crypto.DeterministicKey;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface AddressesFragmentView extends BaseFragmentView{
    void updateAddressList(List<DeterministicKey> deterministicKeys);
    void setAdapterNull();
}
