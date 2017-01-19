package org.qtum.mromanovsky.qtum.btc;

@SuppressWarnings("WeakerAccess")
public class UnspentOutputInfo {
    public final byte[] txHash;
    public final Transaction.Script script;
    public final double value;
    public final int outputIndex;
    public final long confirmations;

    public UnspentOutputInfo(byte[] txHash, Transaction.Script script, double value, int outputIndex, long confirmations) {
        this.txHash = txHash;
        this.script = script;
        this.value = value;
        this.outputIndex = outputIndex;
        this.confirmations = confirmations;
    }
}
