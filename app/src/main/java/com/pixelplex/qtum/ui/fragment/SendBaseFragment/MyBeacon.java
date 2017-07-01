package com.pixelplex.qtum.ui.fragment.SendBaseFragment;

import android.os.Parcel;
import android.os.Parcelable;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;


public class MyBeacon implements Parcelable {

    private Beacon beacon;

    private int count = 0;

    private List<Double> distances = new ArrayList<>();

    private double averageDistance;

    public MyBeacon(Beacon beacon) {
        this.beacon = beacon;
    }


    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public void addDistance(double dim) {
        if (distances.size() < 2) {
            distances.add(dim);

        } else {
            //distances = distances.subList(distances.size() - 2, distances.size() - 1);
            distances.clear();
            distances.add(dim);
        }

        averageDistance = calculateAverageDistance();
    }

    public double getDistance(){
        return calculateAccuracy(beacon.getTxPower(), beacon.getRssi());
    }

    private double calculateAverageDistance() {
        double sum = 0;
        for (Double val : distances) {
            sum += val;
        }
        return sum / distances.size();


    }

    @Override
    public boolean equals(Object that) {

        if (that instanceof MyBeacon) {
            MyBeacon myBeacon = (MyBeacon) that;

            if (getBeacon().getId1().equals(myBeacon.getBeacon().getId1())
                    /*&&
                    getBeacon().getId2().equals(myBeacon.getBeacon().getId2()) &&
                    getBeacon().getId3().equals(myBeacon.getBeacon().getId3())*/
                    ) {
                return true;
            }
        }

        return false;
    }


    private MyBeacon(Parcel in) {
        beacon = in.readParcelable(Beacon.class.getClassLoader());
        count = in.readInt();
        averageDistance = in.readDouble();
    }

    public static final Creator<MyBeacon> CREATOR = new Creator<MyBeacon>() {
        @Override
        public MyBeacon createFromParcel(Parcel in) {
            return new MyBeacon(in);
        }

        @Override
        public MyBeacon[] newArray(int size) {
            return new MyBeacon[size];
        }
    };

    public void incCount() {
        count++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(beacon, flags);
        dest.writeInt(count);
        dest.writeDouble(averageDistance);
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