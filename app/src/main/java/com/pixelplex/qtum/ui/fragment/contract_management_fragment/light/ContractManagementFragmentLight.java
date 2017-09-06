package com.pixelplex.qtum.ui.fragment.contract_management_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.contract_management_fragment.ContractManagementFragment;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;


public class ContractManagementFragmentLight extends ContractManagementFragment {

    @BindView(R.id.tv_contract_address)
    FontTextView tvContractAddress;

    @Override
    protected int getLayout() {
        return R.layout.fragment_contract_management_light;
    }

    @Override
    public void setRecyclerView(List<ContractMethod> contractMethodList, boolean needToGetValue) {
        mMethodAdapter = new MethodAdapter(contractMethodList,R.layout.item_contract_property_light,R.layout.item_contract_method_light, needToGetValue);
        mRecyclerView.setAdapter(mMethodAdapter);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        tvContractAddress.setText(mContractAddress);
    }
}

