package com.pixelplex.qtum.dataprovider.firebase.listeners;


public interface FireBaseTokenRefreshListener {
    void onRefresh(String prevToken, String currentToken);
}
