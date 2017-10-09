package org.qtum.wallet.ui.fragment.smart_contracts_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class SmartContractsPresenterImpl extends BaseFragmentPresenterImpl implements SmartContractsPresenter {

    private SmartContractsView mSmartContractsFragmentView;

    public SmartContractsPresenterImpl(SmartContractsView smartContractsFragmentView) {
        mSmartContractsFragmentView = smartContractsFragmentView;
    }

    @Override
    public SmartContractsView getView() {
        return mSmartContractsFragmentView;
    }
}
