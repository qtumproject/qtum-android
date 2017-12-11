package org.qtum.wallet.entity;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public class HeaderData {

    public static final String INITIAL_BALANCE = "0.0";

    private String address;
    private String balance;
    private String unconfirmedBalance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public void setUnconfirmedBalance(String unconfirmedBalance) {
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
