package com.pixelplex.qtum.ui.fragment.MyContractsFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.fragment.MyContractsFragment.MyContractsFragment;

import java.util.List;


public class MyContractsFragmentDark extends MyContractsFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_contracts;
    }


    @Override
    public void updateRecyclerView(List<Contract> contractList) {
        mContractAdapter = new ContractAdapter(contractList, R.layout.lyt_contract_list_item);
        mRecyclerView.setAdapter(mContractAdapter);
    }

}
