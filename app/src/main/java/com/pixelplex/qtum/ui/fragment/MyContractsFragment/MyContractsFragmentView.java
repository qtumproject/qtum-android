package com.pixelplex.qtum.ui.fragment.MyContractsFragment;

import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


interface MyContractsFragmentView extends BaseFragmentView{
    void updateRecyclerView(List<Contract> contractList);
}
