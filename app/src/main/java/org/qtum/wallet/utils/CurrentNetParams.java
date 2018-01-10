package org.qtum.wallet.utils;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.QtumMainNetParams;
import org.bitcoinj.params.QtumTestNetParams;
import org.qtum.wallet.BuildConfig;

public class CurrentNetParams {

    public  CurrentNetParams(){}

    public static NetworkParameters getNetParams(){
        return BuildConfig.USE_MAIN_NET? QtumMainNetParams.get() : QtumTestNetParams.get();
    }

    public static String getUrl(){
        return "https://testnet-walletapi.qtum.org"; //BuildConfig.API_URL;
    }
}
