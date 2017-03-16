package org.qtum.mromanovsky.qtum.ui.fragment.AddressesFragment;


import android.content.Context;

import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;

import java.util.List;

class AddressesFragmentInteractorImpl implements AddressesFragmentInteractor {


    AddressesFragmentInteractorImpl() {

    }

    @Override
    public List<DeterministicKey> getKeyList() {
        return KeyStorage.getInstance().getKeyList();
    }
}
