package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;

import com.google.common.collect.Lists;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    private Context mContext;
    private Subscription mSubscriptionHistoryList = null;
    private Subscription mSubscriptionBalance = null;

    public WalletFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public List<History> getHistoryList() {
        return HistoryList.getInstance().getHistoryList();
    }

    @Override
    public void setHistoryList(List<History> historyList) {
        HistoryList.getInstance().setHistoryList(historyList);
    }

    @Override
    public void getHistoryList(final GetHistoryListCallBack callBack) {

        mSubscriptionHistoryList = QtumService.newInstance()
                .getHistoryListForSeveralAddresses(KeyStorage.getInstance().getAddresses(), 100,0)
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
                        if(getHistoryList().size()!=0){
                            if(getHistoryList().size()==historyList.size()){
                                callBack.onSuccessWithoutChange();
                                return;
                            }
                        }
                        setHistoryList(historyList);
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

                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {
                        double balance = 0;
                        for(UnspentOutput unspentOutput : unspentOutputs){
                            balance+=unspentOutput.getAmount();
                        }
                        callBack.onSuccess(balance);
                    }
                });

    }

    public void unSubscribe(){
        if(mSubscriptionHistoryList != null){
            mSubscriptionHistoryList.unsubscribe();
        }
        if(mSubscriptionBalance != null){
            mSubscriptionBalance.unsubscribe();
        }
    }

    public interface GetHistoryListCallBack {
        void onSuccess();
        void onSuccessWithoutChange();
        void onError(Throwable e);
    }

    public interface GetBalanceCallBack {
        void onSuccess(double balance);
    }

    @Override
    public String getAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }
}