package com.pixelplex.qtum.ui.fragment.contract_function_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.contract_function_fragment.ContractFunctionFragment;

import java.util.List;


public class ContractFunctionFragmentLight extends ContractFunctionFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_function_detail_light;
    }

    @Override
    public void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList) {
        mParameterAdapter = new ParameterAdapter(contractMethodParameterList,R.layout.lyt_constructor_input_light);
        mParameterList.setAdapter(mParameterAdapter);
    }

}
