package org.qtum.wallet.ui.fragment.template_library_fragment.dark;


import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.fragment.template_library_fragment.TemplateLibraryFragment;

import java.util.List;

public class TemplateLibraryFragmentDark extends TemplateLibraryFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_template_library;
    }

    @Override
    public void setUpTemplateList(List<ContractTemplate> contractTemplateList) {
        initializeRecyclerView(contractTemplateList, org.qtum.wallet.R.layout.item_template);
    }
}
