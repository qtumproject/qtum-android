package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;
import android.support.v4.app.Fragment;

import org.bitcoinj.kits.WalletAppKit;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionFragment;
import org.qtum.mromanovsky.qtum.utils.Transaction;

import java.util.List;

public class WalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletFragmentPresenter {

//    Intent mIntent;
//    UpdateService mUpdateService;
    WalletAppKit mWalletAppKit;

    WalletFragmentInteractorImpl mWalletFragmentInteractor;
    private WalletFragmentView mWalletFragmentView;

    public WalletFragmentPresenterImpl(WalletFragmentView walletFragmentView){
        mWalletFragmentView = walletFragmentView;
        mWalletFragmentInteractor = new WalletFragmentInteractorImpl();
    }

//    ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            Log.d(WalletFragment.TAG, "OnServiceConnected");
//            mUpdateService = ((UpdateService.UpdateBinder) iBinder).getService();
//            mUpdateService.registerListener(mUpdateData);
//            mWalletAppKit = mUpdateService.getWalletAppKit();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
    }

    @Override
    public void onStart(Context context) {
        super.onStart(context);
//        mIntent = new Intent(context,UpdateService.class);
//        Log.d(WalletFragment.TAG, "OnStart");
//        context.bindService(mIntent,mServiceConnection,Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop(Context context) {
        super.onStop(context);
//        Log.d(WalletFragment.TAG, "OnStop");
//        context.unbindService(mServiceConnection);

    }

//    UpdateData mUpdateData = new UpdateData() {
//        @Override
//        public void updateDate() {
//            getView().updatePublicKey(mWalletAppKit.wallet().currentReceiveAddress().toString());
//            getView().updateBalance(mWalletAppKit.wallet().getBalance().toFriendlyString());
//        }
//    };

    @Override
    public WalletFragmentView getView() {
        return mWalletFragmentView;
    }

    public WalletFragmentInteractorImpl getInteractor() {
        return mWalletFragmentInteractor;
    }

    @Override
    public void onClickReceive() {
        Fragment fragment = ReceiveFragment.newInstance();
        getView().openFragment(fragment);
    }

    @Override
    public void openTransactionFragment(int position) {
        Fragment fragment = TransactionFragment.newInstance(position);
        getView().openFragment(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().updateRecyclerView(getInteractor().getTransactionList());
    }

    //    public void onFabClick(){
//        Log.d(WalletFragment.TAG,mWalletAppKit.wallet().getBalance().toFriendlyString() + " " + mWalletAppKit.wallet().getTotalReceived().toFriendlyString());
//        Log.d(WalletFragment.TAG,mWalletAppKit.wallet().getBalance(Wallet.BalanceType.ESTIMATED).toFriendlyString());
//        Log.d(WalletFragment.TAG,"CurrentReceiveAddress: " + mWalletAppKit.wallet().currentReceiveAddress());
//    }
}
