package org.qtum.wallet.ui.fragment.qstore_by_type;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

/**
 * Created by drevnitskaya on 10.10.17.
 */

public interface QStoreByTypePresenter extends BaseFragmentPresenter {
    void searchItems(String s, boolean checked);
}
