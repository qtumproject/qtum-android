package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.dataprovider.UpdateServiceListener;
import org.qtum.mromanovsky.qtum.dataprovider.UpdateService;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionFragment;

class WalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletFragmentPresenter {

    private Intent mIntent;
    private UpdateService mUpdateService;

    private WalletFragmentInteractorImpl mWalletFragmentInteractor;
    private WalletFragmentView mWalletFragmentView;
    private boolean mVisibility = false;

    private final int ONE_PAGE_COUNT = 25;

    WalletFragmentPresenterImpl(WalletFragmentView walletFragmentView) {
        mWalletFragmentView = walletFragmentView;
        mWalletFragmentInteractor = new WalletFragmentInteractorImpl();
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mUpdateService = ((UpdateService.UpdateBinder) iBinder).getService();
            mUpdateService.registerListener(new UpdateServiceListener() {
                @Override
                public void onNewHistory(History history) {
                    if(history.getBlockTime()!=null){
                        int notifyPosition = getInteractor().setHistory(history);
                        getView().notifyConfirmHistory(notifyPosition);
                    }else {
                        getInteractor().addToHistoryList(history);
                        getView().notifyNewHistory();
                    }
                }

                @Override
                public void onChangeBalance(String balance, String unconfirmedBalance) {

                }

                @Override
                public boolean getVisibility() {
                    return mVisibility;
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

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
        mVisibility = true;
        if(mUpdateService!=null) {
            mUpdateService.clearNotification();
        }

    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        mVisibility = false;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        Context context = getView().getContext();
        mIntent = new Intent(context, UpdateService.class);
        if (!isMyServiceRunning(UpdateService.class)) {
            context.startService(mIntent);
        }
        context.bindService(mIntent,mServiceConnection,0);
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
        ((MainActivity) getView().getFragmentActivity()).setRootFragment(sendBaseFragment);
        ((MainActivity) getView().getFragmentActivity()).setIconChecked(3);
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
    public void onLastItem(final int currentItemCount) {
        if(getInteractor().getHistoryList().size()!=getInteractor().getTotalHistoryItem()) {
            getView().loadNewHistory();
            getInteractor().getHistoryList(WalletFragmentInteractorImpl.LOAD_STATE, ONE_PAGE_COUNT,
                    currentItemCount, new WalletFragmentInteractorImpl.GetHistoryListCallBack() {
                        @Override
                        public void onSuccess() {
                            getView().addHistory(currentItemCount, getInteractor().getHistoryList().size() - currentItemCount + 1,
                                    getInteractor().getHistoryList());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        updateData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().getContext().unbindService(mServiceConnection);
        mUpdateService.removeListener();
        getInteractor().unSubscribe();
        getView().setAdapterNull();
    }


    private void loadAndUpdateData() {
        getView().startRefreshAnimation();
        getInteractor().getHistoryList(WalletFragmentInteractorImpl.UPDATE_STATE, ONE_PAGE_COUNT,
                0, new WalletFragmentInteractorImpl.GetHistoryListCallBack() {
            @Override
            public void onSuccess() {
                updateData();
            }

            @Override
            public void onError(Throwable e) {
                getView().stopRefreshRecyclerAnimation();
                Toast.makeText(getView().getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAndUpdateBalance() {
        getInteractor().getBalance(new WalletFragmentInteractorImpl.GetBalanceCallBack() {
            @Override
            public void onSuccess(double balance) {
                getView().updateBalance(balance * (QtumSharedPreference.getInstance().getExchangeRates(getView().getContext())));
            }
        });
    }

    private void updateData() {
        getView().updateHistory(getInteractor().getHistoryList());
    }

}