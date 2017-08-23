package com.pixelplex.qtum.dataprovider.listeners;

import com.pixelplex.qtum.model.gson.store.ContractPurchaseResponse;

/**
 * Created by kirillvolkov on 22.08.17.
 */

public interface ContractPurchaseListener {
    void onContractPurchased(ContractPurchaseResponse purchaseData);
}
