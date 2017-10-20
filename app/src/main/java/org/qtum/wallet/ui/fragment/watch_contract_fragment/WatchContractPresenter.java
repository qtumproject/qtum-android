package org.qtum.wallet.ui.fragment.watch_contract_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

/**
 * Created by drevnitskaya on 05.10.17.
 */

public interface WatchContractPresenter extends BaseFragmentPresenter {
    void onOkClick(String name, String address, String ABIInterface, boolean isToken);

    void onTemplateClick(ContractTemplate contractTemplate);
}
