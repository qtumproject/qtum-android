package org.qtum.mromanovsky.qtum.datastorage;


import android.content.Context;

import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletExtension;
import org.qtum.mromanovsky.qtum.utils.CurrentNetParams;
import org.qtum.mromanovsky.qtum.utils.DictionaryWords;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;


public class KeyStorage {

    private static KeyStorage sKeyStorage;
    private static Wallet sWallet = null;
    private File mFile;
    private Context mContext;

    public static KeyStorage getInstance(Context context){
        if(sKeyStorage == null){
            sKeyStorage = new KeyStorage(context);
        }
        return sKeyStorage;
    }

    private KeyStorage(Context context){
        mContext = context;
        mFile = new File(mContext.getFilesDir().getPath().toString() + "/key_storage");
    }

    public Observable<Wallet> loadWalletFromFile(){
           return Observable.create(new Observable.OnSubscribe<Wallet>() {
               @Override
               public void call(Subscriber<? super Wallet> subscriber) {
                   try {
                       sWallet = Wallet.loadFromFile(mFile, new WalletExtension() {
                           @Override
                           public String getWalletExtensionID() {
                               return null;
                           }

                           @Override
                           public boolean isWalletExtensionMandatory() {
                               return false;
                           }

                           @Override
                           public byte[] serializeWalletExtension() {
                               return new byte[0];
                           }

                           @Override
                           public void deserializeWalletExtension(Wallet containingWallet, byte[] data) throws Exception {

                           }
                       });
                   } catch (UnreadableWalletException e) {
                       e.printStackTrace();
                   }
                   subscriber.onNext(sWallet);
               }
           });
    }

    public Observable<Wallet> createWallet(){
        return Observable.create(new Observable.OnSubscribe<Wallet>() {
            @Override
            public void call(Subscriber<? super Wallet> subscriber) {

                String seedString = "";
                List<String> seedList = new ArrayList<>();
                for(int i=0;i<11;i++) {
                    seedString += DictionaryWords.getRandomWord() + " ";
                    seedList.add(DictionaryWords.getRandomWord());
                }
                seedString += DictionaryWords.getRandomWord();

                String passphrase = "";
                Long creationtime = 1409478661L;
                DeterministicSeed seed = null;
                try {
                    seed = new DeterministicSeed(seedString, null, passphrase, creationtime);
                } catch (UnreadableWalletException e) {
                    e.printStackTrace();
                }
                sWallet = Wallet.fromSeed(CurrentNetParams.getNetParams(),seed);
                //sWallet = new Wallet(CurrentNetParams.getNetParams());
                try {
                    sWallet.saveToFile(mFile);
                    subscriber.onNext(sWallet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                QtumSharedPreference.getInstance().saveSeed(mContext,seedString);
            }
        });
    }

    public Observable<Wallet> importWallet(final String seedString){
        return Observable.create(new Observable.OnSubscribe<Wallet>() {
            @Override
            public void call(Subscriber<? super Wallet> subscriber) {

                String passphrase = "";
                Long creationtime = 1409478661L;
                DeterministicSeed seed = null;
                try {
                    seed = new DeterministicSeed(seedString, null, passphrase, creationtime);
                } catch (UnreadableWalletException e) {
                    e.printStackTrace();
                }
                sWallet = Wallet.fromSeed(CurrentNetParams.getNetParams(),seed);
                //sWallet = new Wallet(CurrentNetParams.getNetParams());
                try {
                    sWallet.saveToFile(mFile);
                    subscriber.onNext(sWallet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                QtumSharedPreference.getInstance().saveSeed(mContext,seedString);
            }
        });
    }

    public Wallet getWallet() {
        return sWallet;
    }

}
