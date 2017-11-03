package org.qtum.wallet.model.gson;


public class CallSmartContractRequest {
    private String[] hashes;
    private String from;

    public CallSmartContractRequest(String[] hashes){
        this.hashes = hashes;
    }

    public CallSmartContractRequest(String[] hashes, String from){
        this.hashes = hashes;
        this.from = from;
    }
}
