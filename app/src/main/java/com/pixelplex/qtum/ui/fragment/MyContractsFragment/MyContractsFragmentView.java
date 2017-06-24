package com.pixelplex.qtum.ui.fragment.MyContractsFragment;

import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by max-v on 6/2/2017.
 */

interface MyContractsFragmentView extends BaseFragmentView{
    void updateRecyclerView(List<Contract> contractList);
}
