package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    Context mContext;

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

        QtumService.newInstance().getHistoryList(QtumSharedPreference.getInstance().getIdentifier(mContext),10,0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<History>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<History> historyList) {
                        callBack.onSuccess(historyList);
                    }
                });
    }

    @Override
    public void getBalance(final GetBalanceCallBack callBack) {
        QtumService.newInstance().getUnspentOutputs(KeyStorage.getInstance(mContext).getWallet().currentReceiveAddress().toString())
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

    public interface GetHistoryListCallBack {
        void onSuccess(List<History> historyList);
    }

    public interface GetBalanceCallBack {
        void onSuccess(double balance);
    }

    @Override
    public String getAddress() {
        return KeyStorage.getInstance(mContext).getWallet().currentReceiveAddress().toString();
    }
}