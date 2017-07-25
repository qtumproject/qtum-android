package com.pixelplex.qtum.ui.fragment.TemplateLibraryFragment;


import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface TemplateLibraryFragmentView extends BaseFragmentView{
    void setUpTemplateList(List<ContractTemplate> contractTemplateList);
    boolean isTokenLibrary();
}
