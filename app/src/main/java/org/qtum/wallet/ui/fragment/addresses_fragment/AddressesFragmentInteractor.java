package org.qtum.wallet.ui.fragment.addresses_fragment;


import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

interface AddressesFragmentInteractor {
    List<DeterministicKey> getKeyList();
}
