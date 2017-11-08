package org.qtum.wallet.ui.fragment.about_fragment;

import org.qtum.wallet.model.Version;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


public interface AboutView extends BaseFragmentView {
    void updateVersion(Version version);
}
