package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.qtum.mromanovsky.qtum.dataprovider.UpdateData;
import org.qtum.mromanovsky.qtum.dataprovider.UpdateService;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionFragment;

class WalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletFragmentPresenter {

    private Intent mIntent;
    private UpdateService mUpdateService;

    private WalletFragmentInteractorImpl mWalletFragmentInteractor;
    private WalletFragmentView mWalletFragmentView;

    WalletFragmentPresenterImpl(WalletFragmentView walletFragmentView) {
        mWalletFragmentView = walletFragmentView;
        mWalletFragmentInteractor = new WalletFragmentInteractorImpl(getView().getContext());
    }


//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            mUpdateService = ((UpdateService.UpdateBinder) iBinder).getService();
//            if(mUpdateService.isMonitoring()){
//                mUpdateService.unsubscribe();
//            }
//            mUpdateService.registerListener(new UpdateData() {
//                @Override
//                public void updateDate() {
//                    mUpdateService.unsubscribe();
//                }
//            });
//            mUpdateService.sendDefaultNotification();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//
//        }
//    };

    @Override
    public void onStart(Context context) {
        super.onStart(context);


        mIntent = new Intent(context, UpdateService.class);
        if(!isMyServiceRunning(UpdateService.class)) {
            //context.startService(mIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getView().getFragmentActivity().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        //context.bindService(mIntent,mServiceConnection,0);
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        if(mUpdateService!=null && !    mUpdateService.isMonitoring()) {
            //mUpdateService.startMonitoringHistory();
        }
    }

    @Override
    public void onStop(Context context) {
        super.onStop(context);
        //context.unbindService(mServiceConnection);
    }

    @Override
    public WalletFragmentView getView() {
        return mWalletFragmentView;
    }

    public WalletFragmentInteractorImpl getInteractor() {
        return mWalletFragmentInteractor;
    }

    @Override
    public void onClickQrCode() {
        SendBaseFragment sendBaseFragment = SendBaseFragment.newInstance(true);
        getView().openRootFragment(sendBaseFragment);
        ((MainActivity)getView().getFragmentActivity()).setRootFragment(sendBaseFragment);
        ((MainActivity)getView().getFragmentActivity()).getBottomNavigationView().getMenu().getItem(3).setChecked(true);
    }

    @Override
    public void onRefresh() {
        loadAndUpdateData();
        loadAndUpdateBalance();
    }

    @Override
    public void sharePubKey() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My QTUM address: " + getInteractor().getAddress());
        emailIntent.setType("text/plain");
        getView().getFragmentActivity().startActivity(emailIntent);
    }

    @Override
    public void openTransactionFragment(int position) {
        Fragment fragment = TransactionFragment.newInstance(position);
        getView().openFragment(fragment);
    }

    @Override
    public void onInitialInitialize() {
        String pubKey = getInteractor().getAddress();
        getView().updatePubKey(pubKey);
        loadAndUpdateData();
        loadAndUpdateBalance();
    }

    @Override
    public void changePage() {
        getInteractor().unSubscribe();
        getView().setAdapterNull();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        updateData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getInteractor().unSubscribe();
        getView().setAdapterNull();
    }

    @Override
    public void onDestroy(Context context) {
        super.onDestroy(context);
    }

    private void loadAndUpdateData(){
        getView().startRefreshAnimation();
        getInteractor().getHistoryList(new WalletFragmentInteractorImpl.GetHistoryListCallBack() {
            @Override
            public void onSuccess() {
                updateData();
            }

            @Override
            public void onError(Throwable e) {
                getView().stopRefreshRecyclerAnimation();
                Toast.makeText(getView().getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAndUpdateBalance(){
        getInteractor().getBalance(new WalletFragmentInteractorImpl.GetBalanceCallBack() {
            @Override
            public void onSuccess(long balance) {
                getView().updateBalance(balance * (QtumSharedPreference.getInstance().getExchangeRates(getView().getContext())));
            }
        });
    }

    private void updateData(){
        getView().updateRecyclerView(getInteractor().getHistoryList());
    }
}