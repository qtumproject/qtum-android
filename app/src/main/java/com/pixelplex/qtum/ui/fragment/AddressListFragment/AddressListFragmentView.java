package com.pixelplex.qtum.ui.fragment.AddressListFragment;

import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.HashMap;
import java.util.List;


interface AddressListFragmentView extends BaseFragmentView{
    void updateAddressList(List<DeterministicKeyWithBalance> deterministicKeyWithBalance, OnAddressClickListener listener);
}
