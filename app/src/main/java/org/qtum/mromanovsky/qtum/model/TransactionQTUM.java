package org.qtum.mromanovsky.qtum.model;


public class TransactionQTUM {
    String category;
    Double amount;
    String address;
    long time;

    public Double getAmount() {
        return amount;
    }

    public String getAddress() {
        return address;
    }

    public long getTime() {
        return time;
    }
}
