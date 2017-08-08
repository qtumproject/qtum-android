package com.pixelplex.qtum.ui.fragment.WatchContractFragment;

import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


interface WatchContractFragmentView extends BaseFragmentView{
    void setABIInterface(String name, String abiInterface);
    boolean isToken();
    void setUpTemplatesList(List<ContractTemplate> contractTemplateList, OnTemplateClickListener listener);
    void notifyAdapter(int adapterPosition);
}
