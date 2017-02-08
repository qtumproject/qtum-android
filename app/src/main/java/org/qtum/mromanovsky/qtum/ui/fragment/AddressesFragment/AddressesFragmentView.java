package org.qtum.mromanovsky.qtum.ui.fragment.AddressesFragment;


import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

public interface AddressesFragmentView extends BaseFragmentView{
    void updateAddressList(List<DeterministicKey> deterministicKeys);
    void setAdapterNull();
}
