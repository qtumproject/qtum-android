package com.pixelplex.qtum.ui.fragment.template_library_fragment;


import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

interface TemplateLibraryFragmentView extends BaseFragmentView{
    void setUpTemplateList(List<ContractTemplate> contractTemplateList);
    boolean isTokenLibrary();
}
