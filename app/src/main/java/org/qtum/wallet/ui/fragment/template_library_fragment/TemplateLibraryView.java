package org.qtum.wallet.ui.fragment.template_library_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface TemplateLibraryView extends BaseFragmentView {

    void setUpTemplateList(List<ContractTemplate> contractTemplateList);

}
