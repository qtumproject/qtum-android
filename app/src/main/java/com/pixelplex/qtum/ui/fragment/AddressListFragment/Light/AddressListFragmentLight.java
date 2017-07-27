package com.pixelplex.qtum.ui.fragment.AddressListFragment.Light;


import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.AddressListFragment;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.AddressesWithBalanceAdapter;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.OnAddressClickListener;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

public class AddressListFragmentLight extends AddressListFragment{

    @Override
    protected int getLayout() {
        return R.layout.fragment_address_list_light;
    }

    @Override
    public void updateAddressList(List<DeterministicKey> deterministicKeys, OnAddressClickListener listener) {
        mAddressesWithBalanceAdapter = new AddressesWithBalanceAdapter(deterministicKeys, listener, R.layout.item_address_light);
        mRecyclerView.setAdapter(mAddressesWithBalanceAdapter);
    }
}
