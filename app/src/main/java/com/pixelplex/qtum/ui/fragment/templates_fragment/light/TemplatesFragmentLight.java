package com.pixelplex.qtum.ui.fragment.templates_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.templates_fragment.TemplatesFragment;

import java.util.List;


public class TemplatesFragmentLight extends TemplatesFragment{

    @Override
    protected int getLayout() {
        return R.layout.fragment_templates_light;
    }

    @Override
    public void setUpTemplateList(List<ContractTemplate> contractTemplateList) {
        initializeRecyclerView(contractTemplateList, R.layout.lyt_contract_list_item_light);
    }

}
