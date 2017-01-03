package org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment;

import android.content.Context;
import android.util.Log;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.listeners.TransactionConfidenceEventListener;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletChangeEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.io.File;


public class StartPageFragmentPresenterImpl extends BaseFragmentPresenterImpl implements StartPageFragmentPresenter {

    StartPageFragmentView mStartPageFragmentView;
    WalletAppKit walletAppKit;

    public StartPageFragmentPresenterImpl(StartPageFragmentView startPageFragmentView){
        mStartPageFragmentView = startPageFragmentView;
    }

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                walletAppKit = new WalletAppKit(TestNet3Params.get(),new File(getView().getContext().getFilesDir().getPath().toString() + "/fileDirectory"), "easy"){
                    @Override
                    protected void onSetupCompleted() {
                        Log.d(StartPageFragment.TAG, "onSetupCompleted");
                        wallet().addCoinsReceivedEventListener(walletCoinsReceivedEventListener);
                        wallet().addChangeEventListener(mWalletChangeEventListener);
                    }
                };

                walletAppKit.startAsync();
                //walletAppKit.awaitRunning();

            }
        });
        thread.start();

    }

    WalletCoinsReceivedEventListener walletCoinsReceivedEventListener = new WalletCoinsReceivedEventListener() {
        @Override
        public void onCoinsReceived(Wallet wallet, Transaction tx, Coin prevBalance, Coin newBalance) {
            Log.d(StartPageFragment.TAG,"onCoinsReceived");
            Log.d(StartPageFragment.TAG,"Wallet: " + wallet.toString());
            Log.d(StartPageFragment.TAG,"Hash tx: " + tx.getHash().toString());
            Log.d(StartPageFragment.TAG,"PrevBalance: " + prevBalance.toFriendlyString());
            Log.d(StartPageFragment.TAG,"NewBalance: " + newBalance.toFriendlyString());


        }
    };

    WalletChangeEventListener mWalletChangeEventListener = new WalletChangeEventListener() {
        @Override
        public void onWalletChanged(Wallet wallet) {
            //when transaction confirmation call this
            Log.d(StartPageFragment.TAG,"WalletChangeEventListener: " + wallet.getBalance().toFriendlyString());
        }
    };


    @Override
    public BaseContextView getView() {
        return mStartPageFragmentView;
    }

    @Override
    public void onClick() {
        Log.d(StartPageFragment.TAG, walletAppKit.wallet().getBalance().toFriendlyString() + " " + walletAppKit.wallet().getTotalReceived().toFriendlyString());
        Log.d(StartPageFragment.TAG, walletAppKit.wallet().getBalance(Wallet.BalanceType.ESTIMATED).toFriendlyString());
        Log.d(StartPageFragment.TAG, "CurrentReceiveAddress: " + walletAppKit.wallet().currentReceiveAddress());
    }
}
