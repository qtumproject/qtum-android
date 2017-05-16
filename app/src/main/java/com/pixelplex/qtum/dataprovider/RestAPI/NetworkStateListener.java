package com.pixelplex.qtum.dataprovider.RestAPI;


public interface NetworkStateListener {
    void onNetworkConnected();
    void onNetworkDisconnected();
}
