package com.pixelplex.qtum.ui.fragment.wallet_fragment;


import com.pixelplex.qtum.dataprovider.rest_api.QtumService;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.history.HistoryResponse;
import com.pixelplex.qtum.model.gson.history.Vin;
import com.pixelplex.qtum.model.gson.history.Vout;
import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;

import java.math.BigDecimal;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    private SubscriptionList mSubscriptionList = new SubscriptionList();
    static final int UPDATE_STATE = 0;
    static final int LOAD_STATE = 1;
    private final List<String> addresses = KeyStorage.getInstance().getAddresses();

    WalletFragmentInteractorImpl(){

    }

    @Override
    public List<History> getHistoryList() {
        return HistoryList.getInstance().getHistoryList();
    }


    @Override
    public void getHistoryList(final int STATE, int limit, int offest, final GetHistoryListCallBack callBack) {

        mSubscriptionList.add(QtumService.newInstance()
                .getHistoryListForSeveralAddresses(addresses, limit, offest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onNext(HistoryResponse historyResponse) {

                        for(History history : historyResponse.getItems()){
                            calculateChangeInBalance(history, addresses);
                        }

                        switch (STATE){
                            case UPDATE_STATE: {
                                HistoryList.getInstance().setHistoryList(historyResponse.getItems());
                                HistoryList.getInstance().setTotalItem(historyResponse.getTotalItems());
                                callBack.onSuccess();
                                break;
                            }
                            case LOAD_STATE:{
                                HistoryList.getInstance().getHistoryList().addAll(historyResponse.getItems());
                                callBack.onSuccess();
                                break;
                            }

                        }

                    }
                }));
    }

    private void calculateChangeInBalance(History history, List<String> addresses){
        BigDecimal changeInBalance = calculateVout(history,addresses).subtract(calculateVin(history,addresses));
        history.setChangeInBalance(changeInBalance);
    }

    private BigDecimal calculateVin(History history, List<String> addresses){
        BigDecimal totalVin = new BigDecimal("0.0");
        boolean equals = false;
        for(Vin vin : history.getVin()){
            for(String address : addresses){
                if(vin.getAddress().equals(address)){
                    vin.setOwnAddress(true);
                    equals = true;
                }
            }
        }
        if(equals){
            totalVin = history.getAmount();
        }
        return totalVin;
    }

    private BigDecimal calculateVout(History history, List<String> addresses){
        BigDecimal totalVout = new BigDecimal("0.0");
        for(Vout vout : history.getVout()){
            for(String address : addresses){
                if(vout.getAddress().equals(address)){
                    vout.setOwnAddress(true);
                    totalVout = totalVout.add(vout.getValue());
                }
            }
        }
        return totalVout;
    }

    @Override
    public int getTotalHistoryItem() {
        return HistoryList.getInstance().getTotalItem();
    }

    @Override
    public void addToHistoryList(History history) {
        calculateChangeInBalance(history,addresses);
        HistoryList.getInstance().getHistoryList().add(0,history);
    }

    @Override
    public Integer setHistory(History history) {
        calculateChangeInBalance(history,addresses);
        for(History historyReplacing : getHistoryList()){
            if(historyReplacing.getTxHash().equals(history.getTxHash())){
                int position = getHistoryList().indexOf(historyReplacing);
                getHistoryList().set(position,history);
                return position;
            }
        }
        getHistoryList().add(0,history);
        return null;
    }

    void unSubscribe(){
        if(mSubscriptionList != null){
            mSubscriptionList.clear();
        }
    }

    interface GetHistoryListCallBack {
        void onSuccess();
        void onError(Throwable e);
    }

    @Override
    public String getAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }
}