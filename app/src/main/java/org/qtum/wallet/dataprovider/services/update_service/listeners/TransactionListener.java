package org.qtum.wallet.dataprovider.services.update_service.listeners;

import org.qtum.wallet.model.gson.history.History;

public interface TransactionListener {
    void onNewHistory(History history);

    boolean getVisibility();
}
