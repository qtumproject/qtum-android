package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.qtum.mromanovsky.qtum.dataprovider.BalanceChangeListener;
import org.qtum.mromanovsky.qtum.dataprovider.NetworkStateReceiver;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.NetworkStateListener;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.dataprovider.TransactionListener;
import org.qtum.mromanovsky.qtum.dataprovider.UpdateService;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendBaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionFragment;

import java.math.BigDecimal;

class WalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletFragmentPresenter {


    private Context mContext;

    private WalletFragmentInteractorImpl mWalletFragmentInteractor;
    private WalletFragmentView mWalletFragmentView;
    private boolean mVisibility = false;
    private UpdateService mUpdateService;
    private NetworkStateReceiver mNetworkStateReceiver;

    private final int ONE_PAGE_COUNT = 25;

    WalletFragmentPresenterImpl(WalletFragmentView walletFragmentView) {
        mWalletFragmentView = walletFragmentView;
        mContext = getView().getContext();
        mWalletFragmentInteractor = new WalletFragmentInteractorImpl();
    }


    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mUpdateService = ((MainActivity) getView().getFragmentActivity()).getUpdateService();

        mUpdateService.starMonitoring();
        mUpdateService.addTransactionListener(new TransactionListener() {
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
            public boolean getVisibility() {
                return mVisibility;
            }
        });

        mUpdateService.addBalanceChangeListener(new BalanceChangeListener() {
            @Override
            public void onChangeBalance() {
                getView().getFragmentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpBalance();
                    }
                });
            }
        });

        mNetworkStateReceiver  = ((MainActivity) getView().getFragmentActivity()).getNetworkReceiver();
        mNetworkStateReceiver.addNetworkStateListener(new NetworkStateListener() {
            @Override
            public void onNetworkConnected() {
                loadAndUpdateData();
            }

            @Override
            public void onNetworkDisconnected() {

            }
        });
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
        setUpBalance();
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
        mNetworkStateReceiver.removeNetworkStateListener();
        mUpdateService.removeTransactionListener();
        mUpdateService.removeBalanceChangeListener();
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
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpBalance() {
        String balance = getInteractor().getBalance();
        if(balance!=null) {
            String unconfirmedBalance = getInteractor().getUnconfirmedBalance();
            if(!unconfirmedBalance.equals("0")) {
                BigDecimal unconfirmedBalanceDecimal = new BigDecimal(unconfirmedBalance);
                BigDecimal balanceDecimal = new BigDecimal(balance);
                getView().updateBalance(getInteractor().getBalance(),balanceDecimal.add(unconfirmedBalanceDecimal).toString());
            } else {
                getView().updateBalance(getInteractor().getBalance(),null);
            }
        }
    }

    private void updateData() {
        getView().updateHistory(getInteractor().getHistoryList());
    }

}