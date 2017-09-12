package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.KeyStorage;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;


public class AddressListFragmentInteractor {

    private Context mContext;

    AddressListFragmentInteractor(Context context){
        mContext = context;
    }

    public List<DeterministicKey> getKeyList() {
        return KeyStorage.getInstance().getKeyList(10);
    }

}
