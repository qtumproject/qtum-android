package org.qtum.mromanovsky.qtum.model;


public class Transaction {
    private String mID;
    private String mDate;
    private double mValue;
    private String mFrom;
    private String mTo;

    public Transaction(String ID, String date, double value, String from, String to) {
        mID = ID;
        mDate = date;
        mValue = value;
        mFrom = from;
        mTo = to;
    }

    public String getID() {
        return mID;
    }

    public String getDate() {
        return mDate;
    }

    public double getValue() {
        return mValue;
    }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }
}
