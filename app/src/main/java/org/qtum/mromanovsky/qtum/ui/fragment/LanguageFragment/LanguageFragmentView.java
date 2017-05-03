package org.qtum.mromanovsky.qtum.ui.fragment.LanguageFragment;


import android.util.Pair;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface LanguageFragmentView extends BaseFragmentView {
    void setUpLanguagesList(List<Pair<String,String>> languagesList);
    void resetText();
}