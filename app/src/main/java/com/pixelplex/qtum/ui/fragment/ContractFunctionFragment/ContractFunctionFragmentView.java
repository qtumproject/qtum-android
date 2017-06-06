package com.pixelplex.qtum.ui.fragment.ContractFunctionFragment;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by max-v on 6/2/2017.
 */

public interface ContractFunctionFragmentView extends BaseFragmentView{
    void setUpParameterList(List<ContractMethodParameter> contractMethodParameterList);
    String getContractTemplateName();
    String getMethodName();
}
