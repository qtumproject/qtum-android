package com.pixelplex.qtum.ui.fragment.watch_contract_fragment;

import com.pixelplex.qtum.model.ContractTemplate;


public interface OnTemplateClickListener {
    void updateSelection(int adapterPosition);
    void onTemplateClick(ContractTemplate contractTemplate);
}
