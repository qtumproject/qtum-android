package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

/**
 * Created by kirillvolkov on 29.06.17.
 */

public class StoreContractPresenter extends BaseFragmentPresenterImpl {

    StoreContractView view;

    public StoreContractPresenter(StoreContractView view) {
        this.view = view;
    }

    @Override
    public StoreContractView getView() {
        return view;
    }
}
