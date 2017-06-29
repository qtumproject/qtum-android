package com.pixelplex.qtum.utils;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;


public class CurrentNetParams {

    public  CurrentNetParams(){}

    public static NetworkParameters getNetParams(){
        return QtumMainNetParams.get();
    }
}
