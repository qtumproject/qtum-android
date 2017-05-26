package com.pixelplex.qtum.ui.fragment.SmartContractConstructorFragment;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public interface SmartContractConstructorView extends BaseFragmentView {

    void onContractConstructorPrepared(List<ContractMethodParameter> params);

}
