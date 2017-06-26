package com.pixelplex.qtum.dataprovider.listeners;


import com.pixelplex.qtum.model.gson.history.History;

public interface TransactionListener {
    void onNewHistory(History history);
    boolean getVisibility();
}
