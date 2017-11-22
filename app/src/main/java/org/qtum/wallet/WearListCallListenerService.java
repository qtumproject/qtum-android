package org.qtum.wallet;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;

import org.qtum.wallet.model.gson.history.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirillvolkov on 21.11.2017.
 */

public class WearListCallListenerService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final String ITEMS = "items";
    public static final String BALANCE = "balance";
    public static final String UNC_BALANCE = "uncBalance";
    public static final String ADDRESS = "address";

    private GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        initGoogleApiClient();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        sendData();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void sendData() {
        new Thread( new Runnable() {
            @Override
            public void run() {

               // NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                //for(Node node : nodes.getNodes()) {
                    List<History> histories = new ArrayList<>();
                    if(QtumApplication.instance != null && QtumApplication.instance.getWearableMessagingProvider() != null) {
                        histories = QtumApplication.instance.getWearableMessagingProvider().getOperations();
                        String balance = QtumApplication.instance.getWearableMessagingProvider().getBalance();
                        String uncBalance = QtumApplication.instance.getWearableMessagingProvider().getUnconfirmedBalance();
                        String address = QtumApplication.instance.getWearableMessagingProvider().getAddress();
                        Gson gson = new Gson();
                        String s = gson.toJson(histories);

                        sendData(s, balance, uncBalance, address);
                    }
                //}
            }

            private void sendData(String items, String balance, String uncBalance, String address) {
                PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data");
                putDataMapReq.getDataMap().putString(ITEMS, items);
                putDataMapReq.getDataMap().putString(BALANCE, balance);
                putDataMapReq.getDataMap().putString(UNC_BALANCE, uncBalance);
                putDataMapReq.getDataMap().putString(ADDRESS, address);
                putDataMapReq = putDataMapReq.setUrgent();
                PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                DataApi.DataItemResult await = Wearable.DataApi.putDataItem(mApiClient, putDataReq).await();
            }

        }).start();
    }
}
