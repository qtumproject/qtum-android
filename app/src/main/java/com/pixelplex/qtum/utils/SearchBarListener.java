package com.pixelplex.qtum.utils;

/**
 * Created by kirillvolkov on 28.06.17.
 */

public interface SearchBarListener {

    void onActivate();
    void onDeactivate();
    void onRequestSearch(String filter);
}
