package com.pixelplex.qtum.ui.fragment.store_contract;

import com.pixelplex.qtum.model.gson.qstore.QstoreContract;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;


public interface StoreContractView extends BaseFragmentView {

    void setContractData(QstoreContract contract);
    void openAbiViewer(String abi);
    void openDetails(String abi);
    void setContractPayStatus(String status);
    void disablePurchase();
}

