package org.qtum.wallet.dataprovider.receivers.network_state_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;

import java.util.ArrayList;
import java.util.List;

public class NetworkStateReceiver extends BroadcastReceiver {

    private List<NetworkStateListener> mNetworkStateListenerList = new ArrayList<>();
    private boolean mInitialState;

    public NetworkStateReceiver(boolean initialState) {
        mInitialState = initialState;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                mInitialState = true;
                Log.i("app", "Network " + ni.getTypeName() + " connected");
                for (NetworkStateListener networkStateListener : mNetworkStateListenerList) {
                    networkStateListener.onNetworkStateChanged(true);
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");
                mInitialState = false;
                for (NetworkStateListener networkStateListener : mNetworkStateListenerList) {
                    networkStateListener.onNetworkStateChanged(false);
                }
            }
        }
    }

    public void addNetworkStateListener(NetworkStateListener networkStateListener) {
        mNetworkStateListenerList.add(networkStateListener);
        networkStateListener.onNetworkStateChanged(mInitialState);
    }

    public void removeNetworkStateListener(NetworkStateListener networkStateListener) {
        mNetworkStateListenerList.remove(networkStateListener);
    }

}
