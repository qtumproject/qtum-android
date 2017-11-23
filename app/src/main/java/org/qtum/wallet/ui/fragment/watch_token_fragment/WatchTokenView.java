package org.qtum.wallet.ui.fragment.watch_token_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface WatchTokenView extends BaseFragmentView {

    void subscribeTokenBalanceChanges(String contractAddress);

    BaseFragment.AlertDialogCallBack getAlertCallback();

}
