package org.qtum.wallet.ui.fragment.my_contracts_fragment.dark;

import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.ui.fragment.my_contracts_fragment.ContractItemListener;
import org.qtum.wallet.ui.fragment.my_contracts_fragment.MyContractsFragment;

import java.util.List;

public class MyContractsFragmentDark extends MyContractsFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_my_contracts;
    }


    @Override
    public void setUpRecyclerView(List<Contract> contractList, ContractItemListener contractItemListener) {
        mContractAdapter = new ContractAdapter(contractList, org.qtum.wallet.R.layout.item_contract_list, contractItemListener);
        mRecyclerView.setAdapter(mContractAdapter);
    }
}
