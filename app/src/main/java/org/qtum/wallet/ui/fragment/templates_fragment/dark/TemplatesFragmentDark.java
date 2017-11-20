package org.qtum.wallet.ui.fragment.templates_fragment.dark;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.fragment.templates_fragment.TemplatesFragment;

import java.util.List;

public class TemplatesFragmentDark extends TemplatesFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_templates;
    }

    @Override
    public void setUpTemplateList(List<ContractTemplate> contractTemplateList) {
        initializeRecyclerView(contractTemplateList, org.qtum.wallet.R.layout.item_template);
    }
}
