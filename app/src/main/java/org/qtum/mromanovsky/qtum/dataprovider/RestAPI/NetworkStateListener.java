package org.qtum.mromanovsky.qtum.dataprovider.RestAPI;


public interface NetworkStateListener {
    void onNetworkConnected();
    void onNetworkDisconnected();
}
