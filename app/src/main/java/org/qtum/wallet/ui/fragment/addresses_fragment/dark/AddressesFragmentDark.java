package org.qtum.wallet.ui.fragment.addresses_fragment.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.addresses_fragment.AddressesFragment;
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
