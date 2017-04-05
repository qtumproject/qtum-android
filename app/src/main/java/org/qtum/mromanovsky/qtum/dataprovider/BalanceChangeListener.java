package org.qtum.mromanovsky.qtum.dataprovider;


public interface BalanceChangeListener {
    void onChangeBalance(String balance, String unconfirmedBalance);
    boolean getVisibility();
}
