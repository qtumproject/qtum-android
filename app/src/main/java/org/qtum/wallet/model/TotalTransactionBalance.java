package org.qtum.wallet.model;


import java.math.BigDecimal;

public class TotalTransactionBalance {

    private BigDecimal ownTotalTransactionBalance;
    private BigDecimal totalTransactionBalance;

    public TotalTransactionBalance(BigDecimal ownTotalTransactionBalance, BigDecimal totalTransactionBalance) {
        this.ownTotalTransactionBalance = ownTotalTransactionBalance;
        this.totalTransactionBalance = totalTransactionBalance;
    }

    public BigDecimal getOwnTotalTransactionBalance() {
        return ownTotalTransactionBalance;
    }

    public void setOwnTotalTransactionBalance(BigDecimal ownTotalTransactionBalance) {
        this.ownTotalTransactionBalance = ownTotalTransactionBalance;
    }

    public BigDecimal getTotalTransactionBalance() {
        return totalTransactionBalance;
    }

    public void setTotalTransactionBalance(BigDecimal totalTransactionBalance) {
        this.totalTransactionBalance = totalTransactionBalance;
    }
}
