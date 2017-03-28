package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;
import android.util.Log;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    private Context mContext;
    private Subscription mSubscriptionHistoryList = null;
    private Subscription mSubscriptionBalance = null;

    WalletFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public List<History> getHistoryList() {
        return HistoryList.getInstance().getHistoryList();
    }


    @Override
    public void getHistoryList(final GetHistoryListCallBack callBack) {

        mSubscriptionHistoryList = QtumService.newInstance()
                .getHistoryListForSeveralAddresses(KeyStorage.getInstance().getAddresses(), 50,0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<History>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onNext(List<History> historyList) {
                        //TODO : edit
//                        if(getHistoryList().size()!=0){
//                            if(getHistoryList().size()==historyList.size()){
//                                callBack.onSuccessWithoutChange();
//                                return;
//                            }
//                        }
                        HistoryList.getInstance().setHistoryList(historyList);
                        QtumSharedPreference.getInstance().saveHistoryCount(mContext,historyList.size());
                        callBack.onSuccess();
                    }
                });

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
                        long balance = 0;
                        for(UnspentOutput unspentOutput : unspentOutputs){
                            balance+=unspentOutput.getAmount();
                        }
                        HistoryList.getInstance().setBalance(balance);
                        callBack.onSuccess(balance);
                    }
                });

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
        void onSuccess(long balance);
    }

    @Override
    public String getAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }
}