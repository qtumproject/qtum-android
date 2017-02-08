package org.qtum.mromanovsky.qtum.ui.fragment.AddressesFragment;


import android.content.Context;

import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;

import java.util.List;

public class AddressesFragmentInteractorImpl implements AddressesFragmentInteractor{

    Context mContext;

    public AddressesFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public List<DeterministicKey> getKeyList() {
        return KeyStorage.getInstance(mContext).getKeyList();
    }
}
