package com.pixelplex.qtum.ui.fragment.SendBaseFragment;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.pixelplex.qtum.ui.activity.BaseActivity.BasePresenterImpl;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivityView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by kirillvolkov on 29.06.17.
 */

public class SendIBeaconPresenter extends BasePresenterImpl {

    MainActivityView view;

    public SendIBeaconPresenter(MainActivityView view) {
        this.view = view;
    }

    @Override
    public MainActivityView getView() {
        return view;
    }

    public void initMonitoring(){
        beaconManager = BeaconManager.getInstanceForApplication(getView().getContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BEACON_LAYOUT));
        beaconManager.bind(beaconConsumer);
    }

    private static final String TAG = "pixelplexservices";

    public static final String DEFINE_MINER_ADDRESS = "b5d8dn3n5b5dfgjju2i2n34hiudciwn2n32zxci";
    public static final String DEFINE_AMOUNT = "100";

    private static final String BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"; //estimote
    private static double MAX_DISTANCE = 0.002d;
    private List<MyBeacon> allBeaconsList = new ArrayList<>();
    private List<MyBeacon> sortedBeaconList = new ArrayList<>();


    BeaconManager beaconManager = null;
    Region rangingRegion = new Region("My Region", null, null, null);

    private BeaconConsumer beaconConsumer = new BeaconConsumer() {
        @Override
        public void onBeaconServiceConnect() {
            startMonitoring();
        }

        @Override
        public Context getApplicationContext() {
            return getView().getContext().getApplicationContext();
        }

        @Override
        public void unbindService(ServiceConnection serviceConnection) {
            getApplicationContext().unbindService(serviceConnection);

        }

        @Override
        public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
            return getApplicationContext().bindService(intent, serviceConnection, i);
        }
    };

    private void startMonitoring() {
        Observable<List<MyBeacon>> rangeNotifierObservable = Observable.create(new Observable.OnSubscribe<List<MyBeacon>>() {
            @Override
            public void call(final Subscriber<? super List<MyBeacon>> subscriber) {
                beaconManager.addRangeNotifier(new RangeNotifier() {
                    @Override
                    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

                        if (collection.size() > 0) {
                            for (Beacon beacon : collection) {
                                if (calculateAccuracy(beacon.getTxPower(),beacon.getRssi()) <= MAX_DISTANCE) {
                                    MyBeacon candidateBeacon = new MyBeacon(beacon);
                                    if (!sortedBeaconList.contains(candidateBeacon)) {
                                        sortedBeaconList.add(candidateBeacon);
                                    } else {
                                        MyBeacon exist = sortedBeaconList.get(sortedBeaconList.indexOf(candidateBeacon));
                                        exist.addDistance(candidateBeacon.getDistance());
                                    }
                                }
                            }
                        }

                        subscriber.onNext(sortedBeaconList);
                    }
                });
                try {
                    beaconManager.startRangingBeaconsInRegion(rangingRegion);
                } catch (RemoteException e) {
                    subscriber.onError(new Throwable(e));
                }

            }
        });


        Observer<List<MyBeacon>> rangeNotifierObserver = new Observer<List<MyBeacon>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<MyBeacon> beacons) {
                if(beacons.size() > 0) {
                    if(beacons.get(0).getBeacon().getId1().toString().equals("b9407f30-f5f8-466e-aff9-25556b57fe6d") &&
                            beacons.get(0).getBeacon().getId2().toString().equals("11757")  &&
                            beacons.get(0).getBeacon().getId3().toString().equals("6633") && calculateAccuracy(beacons.get(0).getBeacon().getTxPower(), beacons.get(0).getBeacon().getRssi()) < 0.002d) {
                        getView().setAdressAndAmount(DEFINE_MINER_ADDRESS, DEFINE_AMOUNT);
                    }
                }
                sortedBeaconList.clear();
            }
        };

        rangeNotifierObservable
                .subscribeOn(Schedulers.newThread())
                .onBackpressureBuffer()
                .observeOn(Schedulers.newThread())
                .subscribe(rangeNotifierObserver);

    }

    private double calculateAccuracy(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0;
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            return (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
    }

}
