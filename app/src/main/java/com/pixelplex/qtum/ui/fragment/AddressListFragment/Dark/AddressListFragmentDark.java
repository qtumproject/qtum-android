package com.pixelplex.qtum.ui.fragment.AddressListFragment.Dark;


import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.AddressListFragment;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.AddressesWithBalanceAdapter;
import com.pixelplex.qtum.ui.fragment.AddressListFragment.OnAddressClickListener;

import java.util.List;

public class AddressListFragmentDark extends AddressListFragment{

    @Override
    protected int getLayout() {
        return R.layout.fragment_address_list;
    }

    @Override
    public void updateAddressList(List<DeterministicKeyWithBalance> deterministicKeyWithBalance, OnAddressClickListener listener) {
        mAddressesWithBalanceAdapter = new AddressesWithBalanceAdapter(deterministicKeyWithBalance, listener,R.layout.item_address);
        mRecyclerView.setAdapter(mAddressesWithBalanceAdapter);
    }
}
