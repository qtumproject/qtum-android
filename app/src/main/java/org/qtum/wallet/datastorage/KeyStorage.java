package org.qtum.wallet.datastorage;


import android.content.Context;

import com.google.common.collect.ImmutableList;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletExtension;
import org.qtum.wallet.utils.DictionaryWords;
import org.qtum.wallet.utils.CurrentNetParams;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class KeyStorage implements Serializable {

    private static KeyStorage sKeyStorage;
    private List<DeterministicKey> mDeterministicKeyList;
    private List<String> mAddressesList;
    private Wallet sWallet = null;
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

    public void setWallet(Wallet wallet) {
        this.sWallet = wallet;
    }

    public void clearKeyStorage() {
        sKeyStorage = null;
    }

    public Observable<String> createWallet() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                String mnemonicCode = "";
                for (int i = 0; i < 11; i++) {
                    mnemonicCode += DictionaryWords.getRandomWord() + " ";
                }
                mnemonicCode += DictionaryWords.getRandomWord();

                String passphrase = "";
                DeterministicSeed seed = null;
                try {
                    seed = new DeterministicSeed(mnemonicCode, null, passphrase, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);
                } catch (UnreadableWalletException e) {
                    e.printStackTrace();
                }
                if (seed != null) {
                    sWallet = Wallet.fromSeed(CurrentNetParams.getNetParams(), seed);

                }
                getKeyList();
                subscriber.onNext(mnemonicCode);
            }
        });
    }

    public Observable<String> importWallet(final String seedString) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                String passphrase = "";
                DeterministicSeed seed = null;
                try {
                    seed = new DeterministicSeed(seedString, null, passphrase, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);

                } catch (UnreadableWalletException e) {
                    e.printStackTrace();
                }
                if (seed != null) {
                    sWallet = Wallet.fromSeed(CurrentNetParams.getNetParams(), seed);
                }
                getKeyList();
                subscriber.onNext(seedString);
            }
        });
    }

    public List<DeterministicKey> getKeyList() {
        if (mDeterministicKeyList == null) {
            mDeterministicKeyList = new ArrayList<>(ADDRESSES_COUNT);
            mAddressesList = new ArrayList<>();
            List<ChildNumber> pathParent = new ArrayList<>();
            pathParent.add(new ChildNumber(88,true));
            pathParent.add(new ChildNumber(0,true));
            for (int i = 0; i < ADDRESSES_COUNT; i++) {
                ImmutableList<ChildNumber> path = HDUtils.append(pathParent, new ChildNumber(i, true));
                DeterministicKey k = sWallet.getActiveKeyChain().getKeyByPath(path,true);
                mDeterministicKeyList.add(k);
                mAddressesList.add(k.toAddress(CurrentNetParams.getNetParams()).toString());
            }
        }
        return mDeterministicKeyList;
    }

    public String getCurrentAddress() {
        return getKeyList().get(sCurrentKeyPosition).toAddress(CurrentNetParams.getNetParams()).toString();
    }

    public List<String> getAddresses() {
        return mAddressesList;
    }

    public DeterministicKey getCurrentKey() {
        return getKeyList().get(sCurrentKeyPosition);
    }

    public void setCurrentKeyPosition(int currentKeyPosition) {
        sCurrentKeyPosition = currentKeyPosition;
    }

    public int getCurrentKeyPosition() {
        return sCurrentKeyPosition;
    }
}