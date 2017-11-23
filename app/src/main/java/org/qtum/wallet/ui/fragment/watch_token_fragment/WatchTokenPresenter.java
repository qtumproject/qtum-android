package org.qtum.wallet.ui.fragment.watch_token_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface WatchTokenPresenter extends BaseFragmentPresenter {

    void onOkClick(String name, String address);

}
