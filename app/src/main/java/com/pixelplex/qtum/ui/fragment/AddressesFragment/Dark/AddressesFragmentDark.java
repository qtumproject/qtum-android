package com.pixelplex.qtum.ui.fragment.AddressesFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.AddressesFragment.AddressesFragment;
import org.bitcoinj.crypto.DeterministicKey;
import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class AddressesFragmentDark extends AddressesFragment {
    @Override
    protected int getLayout() {
        return  R.layout.fragment_addresses;
    }

    @Override
    public void updateAddressList(List<DeterministicKey> deterministicKeys) {
        mAddressAdapter = new AddressesAdapterDark(deterministicKeys, this);
        mRecyclerView.setAdapter(mAddressAdapter);
    }

}
