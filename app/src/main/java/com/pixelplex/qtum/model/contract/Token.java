package com.pixelplex.qtum.model.contract;


public class Token extends Contract {

    private float mLastBalance = 0;

    public Token(String contractAddress, String templateUiid, Boolean hasBeenCreated, String date, String senderAddress, String contractName) {
        super(contractAddress, templateUiid, hasBeenCreated, date, senderAddress, contractName);
        this.mIsSubscribe = true;
    }

    public void setLastBalance(float balance){
        this.mLastBalance = balance;
    }

    public float getLastBalance(){
        return mLastBalance;
    }

    public boolean isSubscribe() {
        return mIsSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        mIsSubscribe = subscribe;
    }

}
