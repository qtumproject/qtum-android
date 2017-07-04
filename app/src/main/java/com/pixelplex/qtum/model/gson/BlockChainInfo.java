
package com.pixelplex.qtum.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockChainInfo {

    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("protocolVersion")
    @Expose
    private Integer protocolVersion;
    @SerializedName("walletVersion")
    @Expose
    private Integer walletVersion;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("blocks")
    @Expose
    private Integer blocks;
    @SerializedName("timeOffset")
    @Expose
    private Integer timeOffset;
    @SerializedName("connections")
    @Expose
    private Integer connections;
    @SerializedName("proxy")
    @Expose
    private String proxy;
    @SerializedName("difficulty")
    @Expose
    private Double difficulty;
    @SerializedName("testNet")
    @Expose
    private Boolean testNet;
    @SerializedName("keyPoolOldest")
    @Expose
    private Integer keyPoolOldest;
    @SerializedName("keyPoolSize")
    @Expose
    private Integer keyPoolSize;
    @SerializedName("payTxFee")
    @Expose
    private Integer payTxFee;
    @SerializedName("relayFee")
    @Expose
    private Double relayFee;
    @SerializedName("errors")
    @Expose
    private String errors;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(Integer protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Integer getWalletVersion() {
        return walletVersion;
    }

    public void setWalletVersion(Integer walletVersion) {
        this.walletVersion = walletVersion;
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

    public Integer getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(Integer timeOffset) {
        this.timeOffset = timeOffset;
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

    public Boolean getTestNet() {
        return testNet;
    }

    public void setTestNet(Boolean testNet) {
        this.testNet = testNet;
    }

    public Integer getKeyPoolOldest() {
        return keyPoolOldest;
    }

    public void setKeyPoolOldest(Integer keyPoolOldest) {
        this.keyPoolOldest = keyPoolOldest;
    }

    public Integer getKeyPoolSize() {
        return keyPoolSize;
    }

    public void setKeyPoolSize(Integer keyPoolSize) {
        this.keyPoolSize = keyPoolSize;
    }

    public Integer getPayTxFee() {
        return payTxFee;
    }

    public void setPayTxFee(Integer payTxFee) {
        this.payTxFee = payTxFee;
    }

    public Double getRelayFee() {
        return relayFee;
    }

    public void setRelayFee(Double relayFee) {
        this.relayFee = relayFee;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

}
