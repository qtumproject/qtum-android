package com.pixelplex.qtum.ui.fragment.templates_fragment;

import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface TemplatesFragmentView extends BaseFragmentView {
    void setUpTemplateList(List<ContractTemplate> contractTemplateList);
}
