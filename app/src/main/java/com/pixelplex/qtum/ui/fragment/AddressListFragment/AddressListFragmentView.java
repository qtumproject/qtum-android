package com.pixelplex.qtum.ui.fragment.AddressListFragment;

import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


interface AddressListFragmentView extends BaseFragmentView{
    void updateAddressList(List<DeterministicKeyWithBalance> deterministicKeyWithBalance, OnAddressClickListener listener);
}
