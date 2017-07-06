package com.pixelplex.qtum.dataprovider.listeners;


public interface FireBaseTokenRefreshListener {
    void onRefresh(String prevToken, String currentToken);
}
