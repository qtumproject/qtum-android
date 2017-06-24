
package com.pixelplex.qtum.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockChainInfo {

    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("protocolversion")
    @Expose
    private Integer protocolversion;
    @SerializedName("walletversion")
    @Expose
    private Integer walletversion;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("blocks")
    @Expose
    private Integer blocks;
    @SerializedName("timeoffset")
    @Expose
    private Integer timeoffset;
    @SerializedName("connections")
    @Expose
    private Integer connections;
    @SerializedName("proxy")
    @Expose
    private String proxy;
    @SerializedName("difficulty")
    @Expose
    private Double difficulty;
    @SerializedName("testnet")
    @Expose
    private Boolean testnet;
    @SerializedName("keypoololdest")
    @Expose
    private Integer keypoololdest;
    @SerializedName("keypoolsize")
    @Expose
    private Integer keypoolsize;
    @SerializedName("paytxfee")
    @Expose
    private Integer paytxfee;
    @SerializedName("relayfee")
    @Expose
    private Double relayfee;
    @SerializedName("errors")
    @Expose
    private String errors;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getProtocolversion() {
        return protocolversion;
    }

    public void setProtocolversion(Integer protocolversion) {
        this.protocolversion = protocolversion;
    }

    public Integer getWalletversion() {
        return walletversion;
    }

    public void setWalletversion(Integer walletversion) {
        this.walletversion = walletversion;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getBlocks() {
        return blocks;
    }

    public void setBlocks(Integer blocks) {
        this.blocks = blocks;
    }

    public Integer getTimeoffset() {
        return timeoffset;
    }

    public void setTimeoffset(Integer timeoffset) {
        this.timeoffset = timeoffset;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean getTestnet() {
        return testnet;
    }

    public void setTestnet(Boolean testnet) {
        this.testnet = testnet;
    }

    public Integer getKeypoololdest() {
        return keypoololdest;
    }

    public void setKeypoololdest(Integer keypoololdest) {
        this.keypoololdest = keypoololdest;
    }

    public Integer getKeypoolsize() {
        return keypoolsize;
    }

    public void setKeypoolsize(Integer keypoolsize) {
        this.keypoolsize = keypoolsize;
    }

    public Integer getPaytxfee() {
        return paytxfee;
    }

    public void setPaytxfee(Integer paytxfee) {
        this.paytxfee = paytxfee;
    }

    public Double getRelayfee() {
        return relayfee;
    }

    public void setRelayfee(Double relayfee) {
        this.relayfee = relayfee;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

}
