package com.pixelplex.qtum.ui.fragment.qtum_cash_management_fragment;

import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface AddressListFragmentView extends BaseFragmentView{
    void updateAddressList(List<DeterministicKeyWithBalance> deterministicKeyWithBalance);
}
