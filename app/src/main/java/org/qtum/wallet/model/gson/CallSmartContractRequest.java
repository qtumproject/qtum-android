package org.qtum.wallet.model.gson;


public class CallSmartContractRequest {
    private String[] hashes;

    public CallSmartContractRequest(String[] hashes){
        this.hashes = hashes;
    }
}
