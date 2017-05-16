package com.pixelplex.qtum.ui.fragment.AddressesFragment;


import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

interface AddressesFragmentInteractor {
    List<DeterministicKey> getKeyList();
}
