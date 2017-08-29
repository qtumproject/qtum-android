package com.pixelplex.qtum.utils;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.QtumMainNetParams;


public class CurrentNetParams {

    public  CurrentNetParams(){}

    public static NetworkParameters getNetParams(){
        return QtumMainNetParams.get();
    }

    public static String getUrl(){
        return "http://163.172.251.4:5931/";
    }
}
