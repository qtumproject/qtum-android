package com.pixelplex.qtum.dataprovider;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;

public interface TransactionListener {
    void onNewHistory(History history);
    boolean getVisibility();
}
