package org.qtum.wallet.ui.fragment.watch_contract_fragment;

import org.qtum.wallet.model.ContractTemplate;


public interface OnTemplateClickListener {
    void updateSelection(int adapterPosition);

    void onTemplateClick(ContractTemplate contractTemplate);
}
