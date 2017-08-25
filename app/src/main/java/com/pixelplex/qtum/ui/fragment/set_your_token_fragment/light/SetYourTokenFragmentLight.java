package com.pixelplex.qtum.ui.fragment.set_your_token_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.set_your_token_fragment.SetYourTokenFragment;

import java.util.List;


public class SetYourTokenFragmentLight extends SetYourTokenFragment {

    private final int LAYOUT = R.layout.fragment_set_your_token_light;

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void onContractConstructorPrepared(List<ContractMethodParameter> params) {
        adapter = new ConstructorAdapterLight(params, this);
        constructorList.setAdapter(adapter);
    }

}
