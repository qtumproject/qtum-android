package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Utils;
import org.qtum.mromanovsky.qtum.btc.BTCUtils;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.utils.CurrentNetParams;

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
                //.getHistoryList(KeyStorage.getInstance().getCurrentAddress(),20,0)
                .getHistoryListForSeveralAddresses(KeyStorage.getInstance().getAddresses(), 20,0)
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
                        setHistoryList(historyList);
                        callBack.onSuccess(historyList);
                    }
                });
    }

    @Override
    public void getBalance(final GetBalanceCallBack callBack) {
        mSubscriptionBalance = QtumService.newInstance()
                //.getUnspentOutputs(KeyStorage.getInstance().getCurrentAddress())
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
                            //TODO: create
                            Address address = new Address(CurrentNetParams.getNetParams(),Utils.parseAsHexOrBase58(unspentOutput.getPubkeyHash()));
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
        void onSuccess(List<History> historyList);
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