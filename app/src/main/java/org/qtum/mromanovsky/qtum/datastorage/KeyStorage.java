package org.qtum.mromanovsky.qtum.datastorage;


import android.content.Context;

import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletExtension;
import org.qtum.mromanovsky.qtum.utils.CurrentNetParams;

import java.io.File;
import java.io.IOException;

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

    public Wallet getWallet() {
        boolean isCreated = QtumSharedPreference.getInstance().getKeyGeneratedInstance(mContext);
        if(!isCreated) {
            {
                sWallet = new Wallet(CurrentNetParams.getNetParams());
                try {
                    sWallet.saveToFile(mFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sWallet;
    }

}
