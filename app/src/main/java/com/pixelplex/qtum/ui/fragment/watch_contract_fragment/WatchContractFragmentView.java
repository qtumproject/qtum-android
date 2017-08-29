package com.pixelplex.qtum.ui.fragment.watch_contract_fragment;

import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface WatchContractFragmentView extends BaseFragmentView{
    void setABIInterface(String name, String abiInterface);
    boolean isToken();
    void setUpTemplatesList(List<ContractTemplate> contractTemplateList, OnTemplateClickListener listener);
    void notifyAdapter(int adapterPosition);
}
