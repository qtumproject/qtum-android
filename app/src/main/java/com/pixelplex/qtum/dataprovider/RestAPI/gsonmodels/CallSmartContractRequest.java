package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels;

/**
 * Created by maksimromanovskij on 05.06.17.
 */

public class CallSmartContractRequest {
    private String[] hashes;

    public CallSmartContractRequest(String[] hashes){
        this.hashes = hashes;
    }
}
