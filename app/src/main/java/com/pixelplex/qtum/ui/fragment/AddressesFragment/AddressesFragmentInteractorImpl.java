package com.pixelplex.qtum.ui.fragment.AddressesFragment;


import org.bitcoinj.crypto.DeterministicKey;
import com.pixelplex.qtum.datastorage.KeyStorage;

import java.util.List;

class AddressesFragmentInteractorImpl implements AddressesFragmentInteractor {


    AddressesFragmentInteractorImpl() {

    }

    @Override
    public List<DeterministicKey> getKeyList() {
        return KeyStorage.getInstance().getKeyList(100);
    }
}
