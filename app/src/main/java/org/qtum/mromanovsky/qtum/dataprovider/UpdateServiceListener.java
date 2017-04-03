package org.qtum.mromanovsky.qtum.dataprovider;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;

public interface UpdateServiceListener {
    void onNewHistory(History history);
}
