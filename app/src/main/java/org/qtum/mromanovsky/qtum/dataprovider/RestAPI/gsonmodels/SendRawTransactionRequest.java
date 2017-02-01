package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;


public class SendRawTransactionRequest {

    private String data;
    private Integer allowHighFee;

    public SendRawTransactionRequest(String data, Integer allowHighFee){
        this.data = data;
        this.allowHighFee = allowHighFee;
    }
}
