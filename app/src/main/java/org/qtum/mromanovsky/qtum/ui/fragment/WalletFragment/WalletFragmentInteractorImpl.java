package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.HistoryResponse;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vin;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vout;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;

import java.math.BigDecimal;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    private Subscription mSubscriptionHistoryList = null;
    private Subscription mSubscriptionBalance = null;
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

        mSubscriptionHistoryList = QtumService.newInstance()
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
                });
    }

    private void calculateChangeInBalance(History history, List<String> addresses){
        BigDecimal changeInBalance = calculateVout(history,addresses).subtract(calculateVin(history,addresses));
        history.setChangeInBalance(changeInBalance.doubleValue());
    }

    private BigDecimal calculateVin(History history, List<String> addresses){
        BigDecimal totalVin = new BigDecimal("0.0");
        for(Vin vin : history.getVin()){
            for(String address : addresses){
                if(vin.getAddress().equals(address)){
                    vin.setOwnAddress(true);
                    totalVin = new BigDecimal(history.getAmount());
                    return totalVin;
                }
            }
        }
        return totalVin;
    }

    private BigDecimal calculateVout(History history, List<String> addresses){
        BigDecimal totalVout = new BigDecimal("0.0");
        for(Vout vout : history.getVout()){
            for(String address : addresses){
                if(vout.getAddress().equals(address)){
                    vout.setOwnAddress(true);
                    totalVout = totalVout.add(new BigDecimal(vout.getValue()));
                }
            }
        }
        return totalVout;
    }

    @Override
    public void getBalance(final GetBalanceCallBack callBack) {
        mSubscriptionBalance = QtumService.newInstance()
                .getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {
                        BigDecimal balance = new BigDecimal("0.0");
                        for(UnspentOutput unspentOutput : unspentOutputs){
                            balance = balance.add(new BigDecimal(unspentOutput.getAmount()));
                        }
                        HistoryList.getInstance().setBalance(balance.toString());
                        callBack.onSuccess(balance.toString());
                    }
                });
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
    public int setHistory(History history) {
        for(History historyReplacing : getHistoryList()){
            if(historyReplacing.getTxHash().equals(history.getTxHash())){
                int position = getHistoryList().indexOf(historyReplacing);
                calculateChangeInBalance(history,addresses);
                getHistoryList().set(position,history);
                return position;
            }
        }
        return 0;
    }

    void unSubscribe(){
        if(mSubscriptionHistoryList != null){
            mSubscriptionHistoryList.unsubscribe();
        }
        if(mSubscriptionBalance != null){
            mSubscriptionBalance.unsubscribe();
        }
    }

    interface GetHistoryListCallBack {
        void onSuccess();
        void onError(Throwable e);
    }

    interface GetBalanceCallBack {
        void onSuccess(String balance);
    }

    @Override
    public String getAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }
}