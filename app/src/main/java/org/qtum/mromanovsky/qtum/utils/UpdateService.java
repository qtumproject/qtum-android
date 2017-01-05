package org.qtum.mromanovsky.qtum.utils;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletTransaction;
import org.bitcoinj.wallet.listeners.WalletChangeEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by max-v on 1/4/2017.
 */

public class UpdateService extends Service {

    WalletAppKit mWalletAppKit;
    private final String TAG = "UpdateService";

    UpdateData mUpdateData = null;

    UpdateBinder mUpdateBinder = new UpdateBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");
        mWalletAppKit = new WalletAppKit(TestNet3Params.get(),
                new File(getBaseContext().getFilesDir().getPath().toString() + "/" + QtumSharedPreference.getInstance().getWalletName(getBaseContext())),
                QtumSharedPreference.getInstance().getWalletName(getBaseContext())){
            @Override
            protected void onSetupCompleted() {
                Log.d(TAG, "onSetupCompleted");
                //mUpdateData.updateDate();
                wallet().addCoinsReceivedEventListener(walletCoinsReceivedEventListener);
                wallet().addChangeEventListener(mWalletChangeEventListener);
            }
        };
        mWalletAppKit.startAsync();
        //mWalletAppKit.awaitRunning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy");
    }

    WalletCoinsReceivedEventListener walletCoinsReceivedEventListener = new WalletCoinsReceivedEventListener() {
        @Override
        public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
            Log.d(TAG,"onCoinsReceived");
            Log.d(TAG,"Wallet: " + wallet.toString());
            Log.d(TAG,"Hash tx: " + tx.getHash().toString());
            Log.d(TAG,"PrevBalance: " + prevBalance.toFriendlyString());
            Log.d(TAG,"NewBalance: " + newBalance.toFriendlyString());


        }
    };

    WalletChangeEventListener mWalletChangeEventListener = new WalletChangeEventListener() {
        @Override
        public void onWalletChanged(Wallet wallet) {
            //when transaction confirmation call this
            //Log.d(TAG,"Wallet: " + wallet.currentReceiveAddress());
            //Log.d(TAG,"WalletChangeEventListener: " + wallet.getBalance().toFriendlyString());
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mUpdateBinder;
    }

    public void registerListener(UpdateData updateData){
        mUpdateData = updateData;
    }

    public WalletAppKit getWalletAppKit() {
        return mWalletAppKit;
    }

    public class UpdateBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}
