package com.pixelplex.qtum.ui.fragment.addresses_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.addresses_fragment.AddressesFragment;
import org.bitcoinj.crypto.DeterministicKey;
import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class AddressesFragmentLight extends AddressesFragment {
    @Override
    protected int getLayout() {
        return  R.layout.fragment_addresses_light;
    }

    @Override
    public void updateAddressList(List<DeterministicKey> deterministicKeys) {
        mAddressAdapter = new AddressesAdapterLight(deterministicKeys, this);
        mRecyclerView.setAdapter(mAddressAdapter);
    }

}
