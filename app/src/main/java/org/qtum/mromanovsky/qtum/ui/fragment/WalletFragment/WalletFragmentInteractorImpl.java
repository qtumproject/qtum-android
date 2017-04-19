package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenParams;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.HistoryResponse;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vin;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vout;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.TokenList;

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
    public String getBalance() {
        return HistoryList.getInstance().getBalance();
    }

    @Override
    public String getUnconfirmedBalance() {
        return HistoryList.getInstance().getUnconfirmedBalance();
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

//    @Override
//    public void newToken(String tokenAddress, final AddToListCallBack addToListCallBack) {
//        QtumService.newInstance().getContractsParams(tokenAddress)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<TokenParams>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                    @Override
//                    public void onNext(TokenParams contractParams) {
//                        addToTokenList(contractParams);
//                        addToListCallBack.onSuccess();
//                    }
//                });
//    }
//
//    public void addToTokenList(TokenParams contractParams){
//        TokenList.getTokenList().addToTokenList(contractParams);
//    }

    public List<TokenParams> getTokenList(){
        return TokenList.getTokenList().getList();
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

//    interface AddToListCallBack{
//        void onSuccess();
//    }

    @Override
    public String getAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }
}