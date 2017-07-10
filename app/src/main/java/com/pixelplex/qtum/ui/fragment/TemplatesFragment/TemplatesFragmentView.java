package com.pixelplex.qtum.ui.fragment.TemplatesFragment;

import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;


public interface TemplatesFragmentView extends BaseFragmentView {
    void setUpTemplateList(List<ContractTemplate> contractTemplateList);
}
