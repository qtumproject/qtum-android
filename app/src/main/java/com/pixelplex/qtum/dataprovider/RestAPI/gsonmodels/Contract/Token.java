package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract;

/**
 * Created by maksimromanovskij on 12.06.17.
 */

public class Token extends Contract {

    private boolean isSubscribe = true;
    private float lastBalance = 0;

    public Token(String contractAddress, String templateName, Boolean hasBeenCreated, Long date, String senderAddress, String contractName) {
        super(contractAddress, templateName, hasBeenCreated, date, senderAddress, contractName);
    }

    public void setLastBalance(float balance){
        this.lastBalance = balance;
    }

    public float getLastBalance(){
        return lastBalance;
    }


    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

}
