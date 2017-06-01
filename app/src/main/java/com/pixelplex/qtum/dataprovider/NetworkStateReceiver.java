package com.pixelplex.qtum.dataprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.pixelplex.qtum.dataprovider.RestAPI.NetworkStateListener;


public class NetworkStateReceiver extends BroadcastReceiver {

    NetworkStateListener mNetworkStateListener;
    private boolean mInitialState;

    public NetworkStateReceiver(boolean initialState){
        mInitialState = initialState;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                mInitialState = true;
                Log.i("app", "Network " + ni.getTypeName() + " connected");
                if(mNetworkStateListener!=null) {
                    mNetworkStateListener.onNetworkStateChanged(true);
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");
                mInitialState = false;
                if(mNetworkStateListener!=null) {
                    mNetworkStateListener.onNetworkStateChanged(false);
                }
            }
        }
    }

    public void addNetworkStateListener(NetworkStateListener networkStateListener){
        mNetworkStateListener = networkStateListener;
        mNetworkStateListener.onNetworkStateChanged(mInitialState);
    }

    public void removeNetworkStateListener(){
        mNetworkStateListener = null;
    }

}
