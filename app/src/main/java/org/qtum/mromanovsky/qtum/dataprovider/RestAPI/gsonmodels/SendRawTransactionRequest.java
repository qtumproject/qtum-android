package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;


public class SendRawTransactionRequest {

    private String mData;
    private Integer mAllowHighFee;

    public SendRawTransactionRequest(String data, Integer allowHighFee){
        this.mData = data;
        this.mAllowHighFee = allowHighFee;
    }
}
