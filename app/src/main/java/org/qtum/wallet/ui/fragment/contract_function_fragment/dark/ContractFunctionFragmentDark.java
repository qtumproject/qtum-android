package org.qtum.wallet.ui.fragment.contract_function_fragment.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ContractFunctionFragment;

import java.util.List;

public class ContractFunctionFragmentDark extends ContractFunctionFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_function_detail;
    }

    @Override
    public void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList) {
        mParameterAdapter = new ParameterAdapter(contractMethodParameterList, R.layout.lyt_constructor_input);
        mParameterList.setAdapter(mParameterAdapter);
    }
}
