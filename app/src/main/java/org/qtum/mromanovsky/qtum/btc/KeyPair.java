package org.qtum.mromanovsky.qtum.btc;

public class KeyPair {
    public final byte[] publicKey;
    public final String address;
    public final BTCUtils.PrivateKeyInfo privateKey;

    public KeyPair(BTCUtils.PrivateKeyInfo privateKeyInfo) {
        if (privateKeyInfo.privateKeyDecoded == null) {
            publicKey = null;
            address = null;
        } else {
            publicKey = BTCUtils.generatePublicKey(privateKeyInfo.privateKeyDecoded, privateKeyInfo.isPublicKeyCompressed);
            address = BTCUtils.publicKeyToAddress(publicKey);
        }
        privateKey = privateKeyInfo;
    }

    public KeyPair(String address, byte[] publicKey, BTCUtils.PrivateKeyInfo privateKey) {
        this.publicKey = publicKey;
        this.address = address;
        this.privateKey = privateKey;
    }
}
