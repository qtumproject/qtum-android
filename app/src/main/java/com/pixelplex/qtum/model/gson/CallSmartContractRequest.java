package com.pixelplex.qtum.model.gson;


public class CallSmartContractRequest {
    private String[] hashes;

    public CallSmartContractRequest(String[] hashes){
        this.hashes = hashes;
    }
}
