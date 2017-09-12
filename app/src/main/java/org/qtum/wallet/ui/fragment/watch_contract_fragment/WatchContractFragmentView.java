package org.qtum.wallet.ui.fragment.watch_contract_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


interface WatchContractFragmentView extends BaseFragmentView {
    void setABIInterface(String name, String abiInterface);
    boolean isToken();
    void setUpTemplatesList(List<ContractTemplate> contractTemplateList, OnTemplateClickListener listener);
    void notifyAdapter(int adapterPosition);
}
