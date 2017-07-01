package com.pixelplex.qtum.utils;


public interface SearchBarListener {

    void onActivate();
    void onDeactivate();
    void onRequestSearch(String filter);
}
