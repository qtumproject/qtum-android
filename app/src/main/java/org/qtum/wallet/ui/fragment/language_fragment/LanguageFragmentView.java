package org.qtum.wallet.ui.fragment.language_fragment;


import android.util.Pair;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

interface LanguageFragmentView extends BaseFragmentView {
    void setUpLanguagesList(List<Pair<String,String>> languagesList);
    void resetText();
}