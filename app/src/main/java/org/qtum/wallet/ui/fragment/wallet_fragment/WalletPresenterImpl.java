package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.support.v4.app.Fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.HistoryPayType;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.transaction_fragment.HistoryType;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionFragment;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.OrderedCollectionChangeSet;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

import static org.qtum.wallet.utils.StringUtils.convertBalanceToString;

public class WalletPresenterImpl extends BaseFragmentPresenterImpl implements WalletPresenter {
    private WalletInteractor mWalletFragmentInteractor;
    private WalletView mWalletView;
    private boolean mVisibility = false;
    private boolean mNetworkConnectedFlag = false;
    private SubscriptionList mSubscriptionList = new SubscriptionList();
    private int visibleItemCount = 0;
    private Integer totalItem;

    private final int ONE_PAGE_COUNT = 25;

    public WalletPresenterImpl(WalletView walletView, WalletInteractor interactor) {
        mWalletView = walletView;
        mWalletFragmentInteractor = interactor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        String pubKey = getInteractor().getAddress();
        getView().updatePubKey(pubKey);

        //List<History> histories = getInteractor().getHistoriesFromDb(0,ONE_PAGE_COUNT);

        getInteractor().addHistoryInDbChangeListener(new HistoryInDbChangeListener<History>() {
            @Override
            public void onHistoryChange(RealmResults<History> histories, @Nullable OrderedCollectionChangeSet changeSet) {
                if (visibleItemCount <= histories.size()) {
                    getView().updateHistory(histories.subList(0, visibleItemCount), changeSet, visibleItemCount);
                }
            }
        });

    }

    private void getHistoriesFromApi(final int start) {
        if (totalItem != null && totalItem == start) {
            getView().hideBottomLoader();
            return;
        }
        getInteractor().getHistoryList(ONE_PAGE_COUNT, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final HistoryResponse historyResponse) {
                        setTotalItem(historyResponse.getTotalItems());


                        for (History history : historyResponse.getItems()) {
                            prepareHistory(history);
                        }
                        visibleItemCount += historyResponse.getItems().size();
                        getInteractor().updateHistoryInRealm(historyResponse.getItems());


                    }
                });
    }

    private void getHistoriesFromRealm() {
        int allCount = getInteractor().getHistoryDbCount();
        if (allCount - visibleItemCount > 0) {
            int toUpdate;
            if (allCount - visibleItemCount > 25) {
                toUpdate = 25;
            } else {
                toUpdate = allCount - visibleItemCount;
            }
            List<History> historiesFromRealm = getInteractor().getHistorySubList(0, visibleItemCount + toUpdate);
            visibleItemCount += toUpdate;
            visibleItemCount = historiesFromRealm.size();
            getView().updateHistory(historiesFromRealm, 0, toUpdate);
        } else {
            getView().hideBottomLoader();
        }
    }

    private void prepareHistory(History history) {
        calculateChangeInBalance(history, getInteractor().getAddresses());
        if (history.getBlockTime() != null) {
            TransactionReceipt transactionReceipt = getInteractor().getReceiptByRxhHashFromRealm(history.getTxHash());
            if (transactionReceipt == null) {
                initTransactionReceipt(history.getTxHash());
            } else {
                history.setReceiptUpdated(true);
                history.setContractType(transactionReceipt.getContractAddress() != null);
            }
        }
    }

    @Override
    public WalletView getView() {
        return mWalletView;
    }

    private WalletInteractor getInteractor() {
        return mWalletFragmentInteractor;
    }

    @Override
    public void onTransactionClick(String txHash) {
        Fragment fragment = TransactionFragment.newInstance(getView().getContext(), txHash, HistoryType.History);
        getView().openFragment(fragment);
    }

    @Override
    public void onLastItem(final int currentItemCount) {
        getView().showBottomLoader();
        if (mNetworkConnectedFlag) {
            getHistoriesFromApi(currentItemCount);
        } else {
            getHistoriesFromRealm();
        }
    }

    private void calculateChangeInBalance(History history, List<String> addresses) {
        BigDecimal totalVin = new BigDecimal("0.0");
        BigDecimal totalVout = new BigDecimal("0.0");
        BigDecimal totalOwnVin = new BigDecimal("0.0");
        BigDecimal totalOwnVout = new BigDecimal("0.0");

        boolean isOwnVin = false;
        boolean isEnemyVout = false;

        for (Vin vin : history.getVin()) {
            vin.setValueString(convertBalanceToString(vin.getValue()));
            for (String address : addresses) {
                if (vin.getAddress().equals(address)) {
                    isOwnVin = true;
                    vin.setOwnAddress(true);
                    totalOwnVin = totalOwnVin.add(vin.getValue());
                }
            }
            totalVin = totalVin.add(vin.getValue());
        }

        for (Vout vout : history.getVout()) {
            vout.setValueString(convertBalanceToString(vout.getValue()));
            for (String address : addresses) {
                if (vout.getAddress().equals(address)) {
                    vout.setOwnAddress(true);
                    totalOwnVout = totalOwnVout.add(vout.getValue());
                }
            }
            if(!vout.isOwnAddress()){
                isEnemyVout = true;
            }
            totalVout = totalVout.add(vout.getValue());
        }

        history.setFee(convertBalanceToString(totalVin.subtract(totalVout)));
        BigDecimal changeInBalance;
        if (isOwnVin) {
            changeInBalance = totalOwnVout.subtract(totalOwnVin).add(totalVin.subtract(totalVout));
        } else {
            changeInBalance = totalOwnVout.subtract(totalOwnVin);
        }
        history.setChangeInBalance(convertBalanceToString(changeInBalance));
        if (!isEnemyVout && isOwnVin) {
            history.setHistoryType(HistoryPayType.Internal_Transaction);
        } else {
            if (changeInBalance.doubleValue() > 0) {
                history.setHistoryType(HistoryPayType.Received);
            } else {
                history.setHistoryType(HistoryPayType.Sent);
            }
        }
    }

    @Override
    public void onNetworkStateChanged(boolean networkConnectedFlag) {
        if (networkConnectedFlag) {
            getView().onlineModeView();
            visibleItemCount = 0;
            getView().clearAdapter();
            getHistoriesFromApi(0);
        } else {
            getView().offlineModeView();
            getHistoriesFromRealm();
        }
        mNetworkConnectedFlag = networkConnectedFlag;
    }

    @Override
    public void onNewHistory(final History history) {
        prepareHistory(history);
        getInteractor().updateHistoryInRealm(history);

    }

    private void initTransactionReceipt(final String txHash) {

        getInteractor().getTransactionReceipt(txHash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TransactionReceipt>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final List<TransactionReceipt> transactionReceipt) {

                        TransactionReceipt transactionReceiptRealm = new TransactionReceipt(txHash);
                        getInteractor().setUpHistoryReceipt(txHash, transactionReceipt.size()>0);

                        if (transactionReceipt.size() > 0) {
                            transactionReceiptRealm = transactionReceipt.get(0);
                        }
                        getInteractor().updateReceiptInRealm(transactionReceiptRealm);

                    }
                });

    }

    @Override
    public boolean getVisibility() {
        return mVisibility;
    }

    @Override
    public void updateVisibility(boolean value) {
        this.mVisibility = value;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriptionList != null) {
            mSubscriptionList.clear();
        }
    }

    public void setNetworkConnectedFlag(boolean mNetworkConnectedFlag) {
        this.mNetworkConnectedFlag = mNetworkConnectedFlag;
    }



    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }
}