package com.pixelplex.qtum.ui.fragment.TemplatesFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.TemplatesFragment.TemplatesFragment;

import java.util.List;


public class TemplatesFragmentDark extends TemplatesFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_templates;
    }

    @Override
    public void setUpTemplateList(List<ContractTemplate> contractTemplateList) {
        initializeRecyclerView(contractTemplateList, R.layout.lyt_contract_list_item);
    }
}
