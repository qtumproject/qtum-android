package org.qtum.mromanovsky.qtum.ui.fragment.AddressesFragment;


import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

public interface AddressesFragmentInteractor {
    List<DeterministicKey> getKeyList();
}
