package org.qtum.wallet.model;

public class Location {
    int locationStart;
    int locationEnd;

    public Location(int locationStart, int locationEnd) {
        this.locationStart = locationStart;
        this.locationEnd = locationEnd;
    }

    public int getLocationEnd() {
        return locationEnd;
    }

    public int getLocationStart() {
        return locationStart;
    }
}
