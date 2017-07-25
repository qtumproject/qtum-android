package com.pixelplex.qtum.ui.fragment.ContractFunctionFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.ContractFunctionFragment.ContractFunctionFragment;

import java.util.List;


public class ContractFunctionFragmentDark extends ContractFunctionFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_function_detail;
    }

    @Override
    public void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList) {
        mParameterAdapter = new ParameterAdapter(contractMethodParameterList,R.layout.lyt_constructor_input);
        mParameterList.setAdapter(mParameterAdapter);
    }

}
