package org.qtum.wallet.ui.fragment.my_contracts_fragment.light;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.ui.fragment.my_contracts_fragment.MyContractsFragment;

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
