package org.qtum.mromanovsky.qtum.utils;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;

public class QtumCryptoGeneratorImpl implements  QtumCryptoGenerator{

    @Override
    public String generateECKey() {
        ECKey ecKey = new ECKey();
        Address address = ecKey.toAddress(MainNetParams.get());
        return address.toString();
    }
}
