package com.pixelplex.qtum.ui.fragment.WatchContractFragment;

import com.pixelplex.qtum.model.ContractTemplate;


public interface OnTemplateClickListener {
    void updateSelection(int adapterPosition);
    void onTemplateClick(ContractTemplate contractTemplate);
}
