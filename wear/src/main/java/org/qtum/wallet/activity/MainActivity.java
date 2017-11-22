package org.qtum.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import org.qtum.wallet.R;
import org.qtum.wallet.adapter.HistoryAdapter;
import org.qtum.wallet.entity.HeaderData;
import org.qtum.wallet.entity.History;
import org.qtum.wallet.listener.HeaderClickListener;
import org.qtum.wallet.listener.ItemClickListener;
import org.qtum.wallet.listtools.CustomScrollingLayoutCallback;
import org.qtum.wallet.storage.DataStorage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener, ItemClickListener, HeaderClickListener {

    public static final String ITEMS = "items";
    public static final String BALANCE = "balance";
    public static final String UNC_BALANCE = "uncBalance";
    public static final String ADDRESS = "address";

    private GoogleApiClient mApiClient;

    @BindView(R.id.history_list)
    WearableRecyclerView listView;

    HistoryAdapter adapter;
    HeaderData headerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        listView.setEdgeItemsCenteringEnabled(true);
        listView.setLayoutManager(
                new WearableLinearLayoutManager(this));

        CustomScrollingLayoutCallback customScrollingLayoutCallback =
                new CustomScrollingLayoutCallback();
        listView.setLayoutManager(
                new WearableLinearLayoutManager(this, customScrollingLayoutCallback));

        headerData = DataStorage.getHeaderData(this);

        adapter = new HistoryAdapter(DataStorage.loadLastHistory(this), headerData, this, this);
        listView.setAdapter(adapter);

        setAmbientEnabled();

        initGoogleApiClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mApiClient != null && !(mApiClient.isConnected() || mApiClient.isConnecting()))
            mApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mApiClient != null) {
            if (mApiClient.isConnected()) {
                mApiClient.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mApiClient != null)
            mApiClient.unregisterConnectionCallbacks(this);
        super.onDestroy();
    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (mApiClient != null && !(mApiClient.isConnected() || mApiClient.isConnecting()))
            mApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(mApiClient, this);
    }

    private void removeListeners() {
        Wearable.DataApi.removeListener(mApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        removeListeners();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        removeListeners();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

        for (DataEvent dataEvent : dataEventBuffer) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                final String content = dataMap.getString(ITEMS);
                String balance = dataMap.getString(BALANCE);
                String uncBalance = dataMap.getString(UNC_BALANCE);
                String address = dataMap.getString(ADDRESS);
                DataStorage.setHeaderData(MainActivity.this, balance, uncBalance, address);
                DataStorage.saveLastHistory(MainActivity.this, content);
                final List<History> histories = DataStorage.loadLastHistory(MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.headerData = DataStorage.getHeaderData(MainActivity.this);
                        adapter.updateHistory(histories);
                    }
                });
            } else if (dataEvent.getType() == DataEvent.TYPE_DELETED) {

            }
        }
    }

    @Override
    public void onItemClick(int adapterPosition) {

    }

    @Override
    public void onHeaderClick(HeaderData headerData) {
        Intent intent = new Intent(this, QrCodeActivity.class);
        intent.putExtra(ADDRESS, headerData.getAddress());
        startActivity(intent);
    }
}
