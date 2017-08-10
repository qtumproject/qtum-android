package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


public interface StoreContractView extends BaseFragmentView {

    void setContractData(QstoreContract contract);
    void openAbiViewer(String abi);
}
