package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class StoreContractPresenter extends BaseFragmentPresenterImpl {

    private StoreContractView view;

    public StoreContractPresenter(StoreContractView view) {
        this.view = view;
    }

    @Override
    public StoreContractView getView() {
        return view;
    }
}
