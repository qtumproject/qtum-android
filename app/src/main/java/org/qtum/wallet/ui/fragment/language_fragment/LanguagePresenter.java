package org.qtum.wallet.ui.fragment.language_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface LanguagePresenter extends BaseFragmentPresenter {
    String getCurrentLanguage();

    void setCurrentLanguage(String language);
}
