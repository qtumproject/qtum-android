package org.qtum.mromanovsky.qtum.dataprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.NetworkStateListener;


public class NetworkStateReceiver extends BroadcastReceiver {

    NetworkStateListener mNetworkStateListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                Log.i("app", "Network " + ni.getTypeName() + " connected");
                if(mNetworkStateListener!=null) {
                    mNetworkStateListener.onNetworkConnected();
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");
                if(mNetworkStateListener!=null) {
                    mNetworkStateListener.onNetworkDisconnected();
                }
            }
        }
    }

    public void addNetworkStateListener(NetworkStateListener networkStateListener){
        mNetworkStateListener = networkStateListener;
    }

    public void removeNetworkStateListener(){
        mNetworkStateListener = null;
    }

}
