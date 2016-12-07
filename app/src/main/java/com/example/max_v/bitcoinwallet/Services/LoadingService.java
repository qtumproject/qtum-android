package com.example.max_v.bitcoinwallet.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.max_v.bitcoinwallet.Activities.MainActivity;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.AbstractWalletEventListener;

import java.io.File;

/**
 * Created by max-v on 12/7/2016.
 */

public class LoadingService extends IntentService{

    WalletAppKit kit;

    public LoadingService(){
        super("LoadingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        kit = new WalletAppKit(RegTestParams.get(), new File(getBaseContext().getFilesDir().getPath().toString() + "/fileDirectory"), "easy") {
            @Override
            protected void onSetupCompleted() {
                // This is called in a background thread after startAndWait is called, as setting up various objects
                // can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
                // on the main thread.

                ECKey ecKey = new ECKey();
                wallet().importKey(ecKey);
                Log.d(MainActivity.MyLog,"ECKey: "+ecKey);
                kit.wallet().addEventListener(new AbstractWalletEventListener() {
                    @Override
                    public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
                        super.onCoinsReceived(wallet, tx, prevBalance, newBalance);
                        Log.d(MainActivity.MyLog,"Prev: "+ prevBalance.getValue());
                        Log.d(MainActivity.MyLog,"New: "+ newBalance.getValue());
                    }
                });
            }
        };

        kit.connectToLocalHost();


        // Download the block chain and wait until it's done.
        kit.startAsync();
        kit.awaitRunning();

    }
}
