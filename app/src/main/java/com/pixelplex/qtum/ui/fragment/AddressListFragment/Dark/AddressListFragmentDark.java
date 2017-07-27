package com.pixelplex.qtum.ui.fragment.AddressListFragment.Dark;


import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.AddressListFragment;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.AddressesWithBalanceAdapter;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.OnAddressClickListener;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

public class AddressListFragmentDark extends AddressListFragment{

    @Override
    protected int getLayout() {
        return R.layout.fragment_address_list;
    }

    @Override
    public void updateAddressList(List<DeterministicKey> deterministicKeys, OnAddressClickListener listener) {
        mAddressesWithBalanceAdapter = new AddressesWithBalanceAdapter(deterministicKeys, listener,R.layout.item_address);
        mRecyclerView.setAdapter(mAddressesWithBalanceAdapter);
    }
}
