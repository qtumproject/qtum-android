package com.pixelplex.qtum.ui.fragment.ContractManagementFragment;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by max-v on 6/2/2017.
 */

public interface ContractManagementFragmentView extends BaseFragmentView{
    void setRecyclerView(List<ContractMethod> contractMethodList);
    String getContractTemplateName();
}
