package org.qtum.wallet.utils;


public interface SearchBarListener {

    void onActivate();
    void onDeactivate();
    void onRequestSearch(String filter);
}
