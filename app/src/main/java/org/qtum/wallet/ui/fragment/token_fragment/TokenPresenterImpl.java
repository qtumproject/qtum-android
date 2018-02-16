package org.qtum.wallet.ui.fragment.token_fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.history.HistoryPayType;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.model.gson.token_history.TokenHistoryResponse;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.transaction_fragment.HistoryType;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionFragment;
import org.qtum.wallet.ui.fragment.wallet_fragment.HistoryInDbChangeListener;

import java.util.List;

import javax.annotation.Nullable;

import io.realm.OrderedCollectionChangeSet;

import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class TokenPresenterImpl extends BaseFragmentPresenterImpl implements TokenPresenter {

    private TokenView view;
    private TokenInteractor interactor;
    private Token token;
    private String abi;
    private SubscriptionList mSubscriptionList = new SubscriptionList();
    private boolean mNetworkConnectedFlag = false;
    private int visibleItemCount = 0;
    private Integer totalItem;

    private final int ONE_PAGE_COUNT = 25;

    public TokenPresenterImpl(TokenView view, TokenInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        setQtumAddress();

        getInteractor().setupPropertyDecimalsValue(token, getView().getDecimalsValueCallback());
        getInteractor().setupPropertySymbolValue(token, getView().getSymbolValueCallback());
        getInteractor().setupPropertyNameValue(token, getView().getNameValueCallback());

        getInteractor().addHistoryInDbChangeListener(new HistoryInDbChangeListener<TokenHistory>() {
            @Override
            public void onHistoryChange(RealmResults<TokenHistory> histories, @Nullable OrderedCollectionChangeSet changeSet) {
                getView().updateHistory(histories.subList(0, visibleItemCount), changeSet, visibleItemCount);
            }
        });
    }

    @Override
    public Token getToken() {
        return token;
    }

    public String getAbi() {
        abi = (getView().isAbiEmpty(abi)) ? getInteractor().readAbiContract(token.getUiid()) : abi;
        return abi;
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    private void setQtumAddress() {
        getView().setQtumAddress(getInteractor().getCurrentAddress());
    }

    @Override
    public TokenView getView() {
        return view;
    }

    public TokenInteractor getInteractor() {
        return interactor;
    }

    @Override
    public void onDecimalsPropertySuccess(String value) {

        if (!TextUtils.isEmpty(value) && !token.getDecimalUnits().equals(Integer.valueOf(value))) {
            //getView().updateHistory(getInteractor().getHistoryList());
        }

        token = getInteractor().setTokenDecimals(token, value);
        getView().setBalance(token.getTokenBalanceWithDecimalUnits().toString());
        getInteractor().setupPropertyTotalSupplyValue(token, getView().getTotalSupplyValueCallback());
    }

    @Override
    public String onTotalSupplyPropertySuccess(Token token, String value) {
        return getInteractor().handleTotalSupplyValue(token, value);
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    @Override
    public void onLastItem(final int currentItemCount) {
        if (mNetworkConnectedFlag) {
            getHistoriesFromApi(currentItemCount);
        } else {
            getHistoriesFromRealm();
        }
    }

    private void getHistoriesFromApi(final int start) {
        if (totalItem != null && totalItem == start) {
            getView().hideBottomLoader();
            return;
        }
        getInteractor().getHistoryList(token.getContractAddress(), ONE_PAGE_COUNT, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TokenHistoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final TokenHistoryResponse historyResponse) {
                        totalItem = historyResponse.getCount();


                                for (TokenHistory history : historyResponse.getItems()) {
                                    prepareHistory(history);
                                }
                                visibleItemCount += historyResponse.getItems().size();
                                getInteractor().updateHistoryInRealm(historyResponse.getItems());
                    }
                });
    }

    private void getHistoriesFromRealm() {
        int allCount = getInteractor().getTokenHistoryDbCount();
        if (allCount - visibleItemCount > 0) {
            int toUpdate;
            if (allCount - visibleItemCount > 25) {
                toUpdate = 25;
            } else {
                toUpdate = allCount - visibleItemCount;
            }
            List<TokenHistory> historiesFromRealm = getInteractor().getTokenHistoryDb(0, visibleItemCount + toUpdate);
            visibleItemCount += toUpdate;
            visibleItemCount = historiesFromRealm.size();
            getView().updateHistory(historiesFromRealm, 0, toUpdate);
        } else {
            getView().hideBottomLoader();
        }
    }

    private void prepareHistory(TokenHistory tokenHistory) {

        List<String> ownAddresses = getInteractor().getAddresses();
        boolean isOwnFrom = false;
        boolean isOwnTo = false;
        for (String address : ownAddresses) {
            if (tokenHistory.getFrom().equals(address)) {
                isOwnFrom = true;
            }
            if (tokenHistory.getTo().equals(address)) {
                isOwnTo = true;
            }
        }

        if (isOwnFrom && isOwnTo) {
            tokenHistory.setHistoryType(HistoryPayType.Internal_Transaction);
        } else {
            if (isOwnFrom) {
                tokenHistory.setHistoryType(HistoryPayType.Sent);
            } else {
                tokenHistory.setHistoryType(HistoryPayType.Received);
            }
        }

        TransactionReceipt transactionReceipt = getInteractor().getReceiptByRxhHashFromRealm(tokenHistory.getTxHash());
        if (transactionReceipt == null) {
            initTransactionReceipt(tokenHistory.getTxHash());
        } else {
            tokenHistory.setReceiptUpdated(true);
        }

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
                        getInteractor().setUpHistoryReceipt(txHash);

                        if (transactionReceipt.size() > 0) {
                            transactionReceiptRealm = transactionReceipt.get(0);
                        }
                        getInteractor().updateReceiptInRealm(transactionReceiptRealm);

                    }
                });

    }

    @Override
    public void onTransactionClick(String txHash) {
        Fragment fragment = TransactionFragment.newInstance(getView().getContext(), txHash, HistoryType.Token_History, token.getDecimalUnits(), token.getSymbol());
        getView().openFragment(fragment);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriptionList != null) {
            mSubscriptionList.clear();
        }
    }
}