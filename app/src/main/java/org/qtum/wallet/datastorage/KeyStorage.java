package org.qtum.wallet.datastorage;

import com.google.common.collect.ImmutableList;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.qtum.wallet.utils.CurrentNetParams;
import org.qtum.wallet.utils.DictionaryWords;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KeyStorage implements Serializable {

    private static KeyStorage sKeyStorage;
    private List<String> mAddressesList;
    private int sCurrentKeyPosition = 0;
    private final int ADDRESSES_COUNT = 10;

    public static KeyStorage getInstance() {
        if (sKeyStorage == null) {
            sKeyStorage = new KeyStorage();
        }
        return sKeyStorage;
    }

    private KeyStorage() {
    }

    public void clearKeyStorage() {
        sKeyStorage = null;
    }

//    public Observable<String> createWallet(final String seedString) {
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//
//                String passphrase = "";
//                DeterministicSeed seed = null;
//                try {
//                    seed = new DeterministicSeed(seedString, null, passphrase, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);
//
//                } catch (UnreadableWalletException e) {
//                    e.printStackTrace();
//                }
//                if (seed != null) {
//                    sWallet = Wallet.fromSeed(CurrentNetParams.getNetParams(), seed);
//                }
//                getKeyList();
//                subscriber.onNext(seedString);
//            }
//        });
//    }

    public String getRandomSeed() {
        String mnemonicCode = "";
        for (int i = 0; i < 11; i++) {
            mnemonicCode += DictionaryWords.getRandomWord() + " ";
        }
        mnemonicCode += DictionaryWords.getRandomWord();
        return mnemonicCode;
    }

    public List<DeterministicKey> getKeyList(String seedString) {

        List<DeterministicKey> mDeterministicKeyList = new ArrayList<>(ADDRESSES_COUNT);
        String passphrase = "";
        DeterministicSeed seed = null;
        try {
            seed = new DeterministicSeed(seedString, null, passphrase, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        KeyChainGroup keyChainGroup = new KeyChainGroup(CurrentNetParams.getNetParams(), seed);
        mAddressesList = new ArrayList<>();
        List<ChildNumber> pathParent = new ArrayList<>();
        pathParent.add(new ChildNumber(88, true));
        pathParent.add(new ChildNumber(0, true));
        for (int i = 0; i < ADDRESSES_COUNT; i++) {
            ImmutableList<ChildNumber> path = HDUtils.append(pathParent, new ChildNumber(i, true));
            DeterministicKey k = keyChainGroup.getActiveKeyChain().getKeyByPath(path, true);
            mDeterministicKeyList.add(k);
            mAddressesList.add(k.toAddress(CurrentNetParams.getNetParams()).toString());
        }

        return mDeterministicKeyList;
    }

    public String loadWallet(String seedString) {

        String passphrase = "";
        DeterministicSeed seed = null;
        try {
            seed = new DeterministicSeed(seedString, null, passphrase, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        KeyChainGroup keyChainGroup = new KeyChainGroup(CurrentNetParams.getNetParams(), seed);
        mAddressesList = new ArrayList<>();
        List<ChildNumber> pathParent = new ArrayList<>();
        pathParent.add(new ChildNumber(88, true));
        pathParent.add(new ChildNumber(0, true));
        for (int i = 0; i < ADDRESSES_COUNT; i++) {
            ImmutableList<ChildNumber> path = HDUtils.append(pathParent, new ChildNumber(i, true));
            DeterministicKey k = keyChainGroup.getActiveKeyChain().getKeyByPath(path, true);
            mAddressesList.add(k.toAddress(CurrentNetParams.getNetParams()).toString());
        }
        return "";
    }

    public String getCurrentAddress() {
        return mAddressesList.get(sCurrentKeyPosition);
    }

    public List<String> getAddresses() {
        return mAddressesList;
    }

    public DeterministicKey getCurrentKey(String seedString) {
        String passphrase = "";
        DeterministicSeed seed = null;
        try {
            seed = new DeterministicSeed(seedString, null, passphrase, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        KeyChainGroup keyChainGroup = new KeyChainGroup(CurrentNetParams.getNetParams(), seed);
        mAddressesList = new ArrayList<>();
        List<ChildNumber> pathParent = new ArrayList<>();
        pathParent.add(new ChildNumber(88, true));
        pathParent.add(new ChildNumber(0, true));
        ImmutableList<ChildNumber> path = HDUtils.append(pathParent, new ChildNumber(sCurrentKeyPosition, true));
        return keyChainGroup.getActiveKeyChain().getKeyByPath(path, true);
    }

    public void setCurrentKeyPosition(int currentKeyPosition) {
        sCurrentKeyPosition = currentKeyPosition;
    }

    public int getCurrentKeyPosition() {
        return sCurrentKeyPosition;
    }
}