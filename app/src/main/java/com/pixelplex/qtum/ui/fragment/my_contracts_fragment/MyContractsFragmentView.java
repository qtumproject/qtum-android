package com.pixelplex.qtum.ui.fragment.my_contracts_fragment;

import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface MyContractsFragmentView extends BaseFragmentView{
    void updateRecyclerView(List<Contract> contractList);
}
