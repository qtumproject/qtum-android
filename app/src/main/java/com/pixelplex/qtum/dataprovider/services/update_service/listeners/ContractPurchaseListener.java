package com.pixelplex.qtum.dataprovider.services.update_service.listeners;

import com.pixelplex.qtum.model.gson.qstore.ContractPurchase;


public interface ContractPurchaseListener {
    void onContractPurchased(ContractPurchase purchaseData);
}
