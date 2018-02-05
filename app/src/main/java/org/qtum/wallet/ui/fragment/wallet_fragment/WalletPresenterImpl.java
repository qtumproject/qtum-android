package org.qtum.wallet.ui.fragment.wallet_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
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
    RealmResults<History> histories;
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

        Realm realm = Realm.getDefaultInstance();
        histories = realm.where(History.class).findAllAsync().sort("blockTime", Sort.DESCENDING);

        histories.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<History>>() {
            @Override
            public void onChange(RealmResults<History> histories, @Nullable OrderedCollectionChangeSet changeSet) {
                getView().updateHistory(histories.subList(0,visibleItemCount), changeSet, visibleItemCount);
            }
        });

    }

    private void getHistoriesFromApi(final int start) {
        if(totalItem!=null && totalItem==start){
            getView().hideBottomLoader();
            return;
        }
        getInteractor().getHistoryList(ONE_PAGE_COUNT, start)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final HistoryResponse historyResponse) {
                        totalItem = historyResponse.getTotalItems();
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                for (History history : historyResponse.getItems()) {
                                    prepareHistory(history, realm);
                                }
                                visibleItemCount += historyResponse.getItems().size();
                                realm.insertOrUpdate(historyResponse.getItems());

                            }
                        });
                    }
                });
    }

    private void getHistoriesFromRealm(){
        int allCount = histories.size();
        if(allCount-visibleItemCount>0) {
            int toUpdate;
            if(allCount-visibleItemCount>25){
                toUpdate = 25;
            }else{
                toUpdate = allCount-visibleItemCount;
            }
            List<History> historiesFromRealm = histories.subList(0, visibleItemCount + toUpdate);
            visibleItemCount += toUpdate;
            visibleItemCount = historiesFromRealm.size();
            getView().updateHistory(historiesFromRealm, 0, toUpdate);
        } else {
            getView().hideBottomLoader();
        }
    }

    private void prepareHistory(History history, Realm realm) {
        calculateChangeInBalance(history, getInteractor().getAddresses());
        if (history.getBlockTime() != null) {
            TransactionReceipt transactionReceipt = realm.where(TransactionReceipt.class).equalTo("transactionHash", history.getTxHash()).findFirst();
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
    public void openTransactionFragment(String txHash) {
        getView().openTransactionsFragment(txHash);
    }

    @Override
    public void onLastItem(final int currentItemCount) {
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
            totalVout = totalVout.add(vout.getValue());
        }

        history.setFee(convertBalanceToString(totalVin.subtract(totalVout)));
        if (isOwnVin) {
            history.setChangeInBalance(convertBalanceToString(totalOwnVout.subtract(totalOwnVin).add(totalVin.subtract(totalVout))));
        } else {
            history.setChangeInBalance(convertBalanceToString(totalOwnVout.subtract(totalOwnVin)));
        }
    }

    @Override
    public void onNetworkStateChanged(boolean networkConnectedFlag) {
            if(networkConnectedFlag) {
                visibleItemCount = 0;
                    getView().clearAdapter();
                getHistoriesFromApi(0);
            } else {
                getHistoriesFromRealm();
            }
        mNetworkConnectedFlag = networkConnectedFlag;
    }

    @Override
    public void onNewHistory(final History history) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                prepareHistory(history, realm);
                realm.insertOrUpdate(history);
            }
        });
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
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                TransactionReceipt transactionReceiptRealm = new TransactionReceipt(txHash);
                                History history = realm.where(History.class).equalTo("txHash", txHash).findFirst();
                                history.setReceiptUpdated(true);
                                if (transactionReceipt.size() > 0) {
                                    transactionReceiptRealm = transactionReceipt.get(0);
                                    history.setContractType(true);
                                } else {
                                    history.setContractType(false);
                                }
                                realm.insertOrUpdate(history);
                                realm.insertOrUpdate(transactionReceiptRealm);
                            }
                        });
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
}