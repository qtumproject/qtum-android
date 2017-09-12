package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TransactionListener;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.qtum_cash_management_fragment.AddressListFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveFragment;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionFragment;

import java.math.BigDecimal;

public class WalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements WalletFragmentPresenter {


    private Context mContext;

    private WalletFragmentInteractorImpl mWalletFragmentInteractor;
    private WalletFragmentView mWalletFragmentView;
    private boolean mVisibility = false;
    private UpdateService mUpdateService;
    private NetworkStateReceiver mNetworkStateReceiver;
    private boolean mNetworkConnectedFlag = false;
    private boolean OPEN_QR_CODE_FRAGMENT_FLAG = false;

    private final int ONE_PAGE_COUNT = 25;
    private static final int REQUEST_CAMERA = 3;

    public WalletFragmentPresenterImpl(WalletFragmentView walletFragmentView) {
        mWalletFragmentView = walletFragmentView;
        mContext = getView().getContext();
        mWalletFragmentInteractor = new WalletFragmentInteractorImpl();
    }


    @Override
    public void onViewCreated() {
        super.onViewCreated();

        getView().getMainActivity().subscribeServiceConnectionChangeEvent(new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if(isConnecting){
                    mUpdateService = getView().getMainActivity().getUpdateService();
                    mUpdateService.addTransactionListener(new TransactionListener() {
                        @Override
                        public void onNewHistory(History history) {
                            if(history.getBlockTime()!=null){
                                Integer notifyPosition = getInteractor().setHistory(history);
                                if(notifyPosition==null){
                                    getView().notifyNewHistory();
                                } else {
                                    getView().notifyConfirmHistory(notifyPosition);
                                }
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

                   initBalanceListener();
                }
            }
        });

        mNetworkStateReceiver  = getView().getMainActivity().getNetworkReceiver();
        mNetworkStateReceiver.addNetworkStateListener(new NetworkStateListener() {

            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                mNetworkConnectedFlag = networkConnectedFlag;
                if(networkConnectedFlag){
                    loadAndUpdateData();
                }
            }
        });

        getView().getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if(requestCode == REQUEST_CAMERA) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        OPEN_QR_CODE_FRAGMENT_FLAG = true;
                    }
                }
            }
        });
    }

    public void initBalanceListener(){
        mUpdateService.addBalanceChangeListener(new BalanceChangeListener() {
            @Override
            public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
                getView().getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String balanceString = balance.toString();
                        if(balanceString!=null) {
                            String unconfirmedBalanceString = unconfirmedBalance.toString();
                            if(!TextUtils.isEmpty(unconfirmedBalanceString) && !unconfirmedBalanceString.equals("0")) {
                                getView().updateBalance(balanceString,String.valueOf(unconfirmedBalance.floatValue()));
                            } else {
                                getView().updateBalance(balanceString,null);
                            }
                        }
                    }
                });
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
        if(OPEN_QR_CODE_FRAGMENT_FLAG){
            openQrCodeFragment();
        }
    }

    public void notifyHeader() {
        String pubKey = getInteractor().getAddress();
        getView().updatePubKey(pubKey);
        loadAndUpdateData();
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

    private WalletFragmentInteractorImpl getInteractor() {
        return mWalletFragmentInteractor;
    }

    @Override
    public void onClickQrCode() {
        if(getView().getMainActivity().checkPermission(Manifest.permission.CAMERA)){
            openQrCodeFragment();
        } else {
            getView().getMainActivity().loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);
        }
    }

    private void openQrCodeFragment(){
        OPEN_QR_CODE_FRAGMENT_FLAG = false;
        SendFragment sendFragment = (SendFragment) SendFragment.newInstance(true,null,null, null,mContext);
        getView().openRootFragment(sendFragment);
        getView().getMainActivity().setRootFragment(sendFragment);
    }

    public void onReceiveClick(){
        BaseFragment receiveFragment = ReceiveFragment.newInstance(mContext,null, null);
        getView().openFragmentForResult(receiveFragment);
    }

    @Override
    public void onChooseAddressClick() {
        BaseFragment addressListFragment = AddressListFragment.newInstance(mContext);
        getView().openFragment(addressListFragment);
    }

    @Override
    public void onRefresh() {
        if(mNetworkConnectedFlag) {
            loadAndUpdateData();
        }else{
            getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.no_internet_connection),mContext.getString(org.qtum.wallet.R.string.please_check_your_network_settings), mContext.getString(org.qtum.wallet.R.string.ok), BaseFragment.PopUpType.error);
            getView().stopRefreshRecyclerAnimation();
        }
    }

    @Override
    public void sharePubKey() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My QTUM address: " + getInteractor().getAddress());
        emailIntent.setType("text/plain");
        getView().getMainActivity().startActivity(emailIntent);
    }

    @Override
    public void openTransactionFragment(int position) {
        Fragment fragment = TransactionFragment.newInstance(getView().getContext(), position);
        getView().openFragment(fragment);
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
        getInteractor().unSubscribe();
        mNetworkStateReceiver.removeNetworkStateListener();
        mUpdateService.removeTransactionListener();
        mUpdateService.removeBalanceChangeListener();
        getView().getMainActivity().removePermissionResultListener();
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

    private void updateData() {
        getView().updateHistory(getInteractor().getHistoryList());
    }

}