package org.qtum.wallet.model.contract;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class Contract implements Serializable {

    @SerializedName("contractAddress")
    protected String mContractAddress;
    @SerializedName("uiid")
    protected String mUiid;
    @SerializedName("contractName")
    protected String mContractName;
    @SerializedName("creationStatus")
    protected ContractCreationStatus mCreationStatus;
    @SerializedName("date")
    protected Long mDate;
    @SerializedName("senderAddress")
    protected String mSenderAddress;
    @SerializedName("isSubscribe")
    protected boolean mIsSubscribe;

    public Contract(String contractAddress, String uiid, ContractCreationStatus creationStatus, Long date, String senderAddress, String contractName) {
        this.mContractAddress = contractAddress;
        this.mUiid = uiid;
        this.mCreationStatus = creationStatus;
        this.mContractName = contractName;
        this.mDate = date;
        this.mSenderAddress = senderAddress;
        this.mIsSubscribe = false;
    }

    public Contract(String contractAddress, String uiid, String contractName,
                    ContractCreationStatus creationStatus, Long date, String senderAddress, boolean isSubscribe) {
        mContractAddress = contractAddress;
        mUiid = uiid;
        mContractName = contractName;
        mCreationStatus = creationStatus;
        mDate = date;
        mSenderAddress = senderAddress;
        mIsSubscribe = isSubscribe;
    }

    /**
     * Default constructor for unit testing
     */
    public Contract() {
    }


    /**
     * Constructor for unit testing
     */
    public Contract(boolean isSubscribe) {
        this.mIsSubscribe = isSubscribe;
    }

    /**
     * Constructor for unit testing
     */
    public Contract(String contractAddress) {
        this.mContractAddress = contractAddress;
    }

    /**
     * Constructor for unit testing
     */
    public Contract(boolean isSubscribe, String contractAddress) {
        this.mIsSubscribe = isSubscribe;
        this.mContractAddress = contractAddress;
    }

    public String getContractAddress() {
        return mContractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.mContractAddress = contractAddress;
    }

    public String getUiid() {
        return mUiid;
    }

    public void setUiid(String uiid) {
        this.mUiid = uiid;
    }

    public ContractCreationStatus getCreationStatus() {
        return mCreationStatus;
    }

    public void setCreationStatus(ContractCreationStatus creationStatus) {
        mCreationStatus = creationStatus;
    }

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long date) {
        this.mDate = date;
    }

    public String getContractName() {
        return mContractName;
    }

    public void setContractName(String contractName) {
        this.mContractName = contractName;
    }

    public String getSenderAddress() {
        return mSenderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.mSenderAddress = senderAddress;
    }

    public boolean isSubscribe() {
        return mIsSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        mIsSubscribe = subscribe;
    }
}
