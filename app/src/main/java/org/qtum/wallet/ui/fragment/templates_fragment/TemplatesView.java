package org.qtum.wallet.ui.fragment.templates_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface TemplatesView extends BaseFragmentView {
    void setUpTemplateList(List<ContractTemplate> contractTemplateList);
}
