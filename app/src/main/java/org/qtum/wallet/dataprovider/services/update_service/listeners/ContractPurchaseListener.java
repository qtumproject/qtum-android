package org.qtum.wallet.dataprovider.services.update_service.listeners;

import org.qtum.wallet.model.gson.qstore.ContractPurchase;


public interface ContractPurchaseListener {
    void onContractPurchased(ContractPurchase purchaseData);
}
