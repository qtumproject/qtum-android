package org.qtum.wallet.model;

public class TransactionHashWithSender {
    String transactionHash;
    String sender;

    public TransactionHashWithSender(String transactionHash, String sender) {
        this.transactionHash = transactionHash;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getTransactionHash() {
        return transactionHash;
    }
}
