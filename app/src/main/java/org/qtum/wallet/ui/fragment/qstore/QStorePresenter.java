package org.qtum.wallet.ui.fragment.qstore;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

/**
 * Created by drevnitskaya on 10.10.17.
 */

public interface QStorePresenter extends BaseFragmentPresenter {
    void searchItems(String seacrhBarText, boolean checked);

}
