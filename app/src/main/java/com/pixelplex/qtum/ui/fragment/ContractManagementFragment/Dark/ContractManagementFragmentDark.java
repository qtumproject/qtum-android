package com.pixelplex.qtum.ui.fragment.ContractManagementFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.ContractManagementFragment.ContractManagementFragment;

import java.util.List;


public class ContractManagementFragmentDark extends ContractManagementFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_contract_management;
    }

    @Override
    public void setRecyclerView(List<ContractMethod> contractMethodList, boolean needToGetValue) {
        mMethodAdapter = new MethodAdapter(contractMethodList,R.layout.item_contract_property,R.layout.item_contract_method, needToGetValue);
        mRecyclerView.setAdapter(mMethodAdapter);
    }

}
