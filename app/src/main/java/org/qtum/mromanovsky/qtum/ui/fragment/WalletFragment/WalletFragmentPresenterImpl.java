package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.JSONRPCThreadedClient;
import org.qtum.mromanovsky.qtum.model.History;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionFragment;

import java.util.List;

public class WalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletFragmentPresenter {

//    Intent mIntent;
//    UpdateService mUpdateService;
//    WalletAppKit mWalletAppKit;

    WalletFragmentInteractorImpl mWalletFragmentInteractor;
    private WalletFragmentView mWalletFragmentView;

    public WalletFragmentPresenterImpl(WalletFragmentView walletFragmentView) {
        mWalletFragmentView = walletFragmentView;
        mWalletFragmentInteractor = new WalletFragmentInteractorImpl(getView().getContext());
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
//            getView().updatePublicKey(mWalletAppKit.ic_wallet().currentReceiveAddress().toString());
//            getView().updateBalance(mWalletAppKit.ic_wallet().getBalance().toFriendlyString());
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
        getView().openFragmentAndAddToBackStack(fragment);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }


    @Override
    public void openTransactionFragment(int position) {
        Fragment fragment = TransactionFragment.newInstance(position);
        getView().openFragmentAndAddToBackStack(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        String pubKey = getInteractor().getPubKey();
        getView().initializeDynamicDataViews(pubKey);
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        getView().updateRecyclerView(getInteractor().getTransactionList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAdapterNull();
    }

    private void refreshData(){
        getInteractor().getBalance(new JSONRPCThreadedClient.OnJSONArrayResultListener() {
            @Override
            public void manageResult(JSONArray result) {

            }

            @Override
            public void manageResult(Object result) {
                Gson gson = new Gson();
                double balance = 0;
                List<History> historyList = gson.fromJson(result.toString(),new TypeToken<List<History>>(){}.getType());
                for(History history : historyList){
                    balance += history.getAmount();
                }
                getView().updateBalance(balance);
            }

            @Override
            public void sendErrorMessageNull() {

            }

            @Override
            public void sendError(Exception content) {

            }
        });
    }

    //    public void onFabClick(){
//        Log.d(WalletFragment.TAG,mWalletAppKit.ic_wallet().getBalance().toFriendlyString() + " " + mWalletAppKit.ic_wallet().getTotalReceived().toFriendlyString());
//        Log.d(WalletFragment.TAG,mWalletAppKit.ic_wallet().getBalance(Wallet.BalanceType.ESTIMATED).toFriendlyString());
//        Log.d(WalletFragment.TAG,"CurrentReceiveAddress: " + mWalletAppKit.ic_wallet().currentReceiveAddress());
//    }
}
