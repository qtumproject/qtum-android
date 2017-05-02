package org.qtum.mromanovsky.qtum.ui.fragment.LanguageFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface LanguageFragmentView extends BaseFragmentView {
    void setUpLanguagesList(List<String> languagesList);
    void resetText();
}