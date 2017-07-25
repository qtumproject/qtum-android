package com.pixelplex.qtum.ui.fragment.MyContractsFragment.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.fragment.MyContractsFragment.MyContractsFragment;

import java.util.List;


public class MyContractsFragmentLight extends MyContractsFragment{

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_contracts_light;
    }

    @Override
    public void updateRecyclerView(List<Contract> contractList) {
        mContractAdapter = new ContractAdapter(contractList, R.layout.lyt_contract_list_item_light);
        mRecyclerView.setAdapter(mContractAdapter);
    }

}
