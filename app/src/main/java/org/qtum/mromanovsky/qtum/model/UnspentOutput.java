package org.qtum.mromanovsky.qtum.model;

public class UnspentOutput {

    private String txid;
    private int vout;
    private String scriptPubKey;
    private double amount;
    private long confirmations;

    public String getTxid() {
        return txid;
    }

    public int getVout() {
        return vout;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public double getAmount() {
        return amount;
    }

    public long getConfirmations() {
        return confirmations;
    }
}
