package org.qtum.wallet.ui.fragment.addresses_fragment;


import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.wallet.datastorage.KeyStorage;

import java.util.List;

class AddressesFragmentInteractorImpl implements AddressesFragmentInteractor {


    AddressesFragmentInteractorImpl() {

    }

    @Override
    public List<DeterministicKey> getKeyList() {
        return KeyStorage.getInstance().getKeyList(10);
    }
}
