package com.pixelplex.qtum.ui.fragment.contract_management_fragment.light;

import android.widget.Button;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.contract_management_fragment.ContractManagementFragment;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ContractManagementFragmentLight extends ContractManagementFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_contract_management_light;
    }

    @Override
    public void setRecyclerView(List<ContractMethod> contractMethodList, boolean needToGetValue) {
        mMethodAdapter = new MethodAdapter(contractMethodList,R.layout.item_contract_property_light,R.layout.item_contract_method_light, needToGetValue);
        mRecyclerView.setAdapter(mMethodAdapter);
    }

}

