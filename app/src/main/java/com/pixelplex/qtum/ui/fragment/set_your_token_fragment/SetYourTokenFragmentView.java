package com.pixelplex.qtum.ui.fragment.set_your_token_fragment;

import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface SetYourTokenFragmentView extends BaseFragmentView {

    void onContractConstructorPrepared(List<ContractMethodParameter> params);

}
