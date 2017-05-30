package com.pixelplex.qtum.dataprovider.RestAPI;


public interface NetworkStateListener {
    void onNetworkStateChanged(boolean networkConnectedFlag);
}
