package com.pixelplex.qtum.utils;

import org.bitcoinj.core.Utils;
import org.bitcoinj.params.AbstractBitcoinNetParams;

import static com.google.common.base.Preconditions.checkState;


public class QtumTestNetParams extends AbstractBitcoinNetParams {

    public QtumTestNetParams(){
        super();
        id = ID_TESTNET;
        // Genesis hash is 000000000933ea01ad0ee984209779baaec3ced90fa3f408719526f8d77f4943
        packetMagic = 0x0b110907;
        interval = INTERVAL;
        targetTimespan = TARGET_TIMESPAN;
        maxTarget = Utils.decodeCompactBits(0x1d00ffffL);
        port = 18333;
        addressHeader = 120;
        p2shHeader = 110;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        dumpedPrivateKeyHeader = 239;
        genesisBlock.setTime(1296688602L);
        genesisBlock.setDifficultyTarget(0x1d00ffffL);
        genesisBlock.setNonce(414098458);
        spendableCoinbaseDepth = 100;
        subsidyDecreaseBlockCount = 210000;
        String genesisHash = genesisBlock.getHashAsString();
        checkState(genesisHash.equals("000000000933ea01ad0ee984209779baaec3ced90fa3f408719526f8d77f4943"));
        alertSigningKey = Utils.HEX.decode("04302390343f91cc401d56d68b123028bf52e5fca1939df127f63c6467cdf9c8e2c14b61104cf817d0b780da337893ecc4aaff1309e536162dabbdb45200ca2b0a");

        dnsSeeds = new String[] {
                "testnet-seed.bitcoin.schildbach.de", // Andreas Schildbach
                "testnet-seed.bitcoin.petertodd.org"  // Peter Todd
        };
        addrSeeds = null;
        bip32HeaderPub = 0x043587CF;
        bip32HeaderPriv = 0x04358394;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_TESTNET;
    }

    private static QtumTestNetParams instance;
    public static synchronized QtumTestNetParams get(){
        if (instance == null) {
            instance = new QtumTestNetParams();
        }
        return instance;
    }
}
