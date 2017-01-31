package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClientImpl;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.datastorage.TransactionQTUMList;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletFragmentInteractorImpl implements WalletFragmentInteractor {

    Context mContext;
    QtumJSONRPCClientImpl mQtumJSONRPCClient;

    public WalletFragmentInteractorImpl(Context context){
        mContext = context;
        mQtumJSONRPCClient = new QtumJSONRPCClientImpl();
    }

    @Override
    public List<TransactionQTUM> getTransactionList() {
        return TransactionQTUMList.getInstance().getTransactionQTUMList();
    }

    @Override
    public void getTransaction(final GetDataCallBack callBack) {

        mQtumJSONRPCClient.getTransactions(QtumSharedPreference.getInstance().getIdentifier(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TransactionQTUM>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<TransactionQTUM> transactionQTUMList) {
                        callBack.onSuccess(transactionQTUMList);
                    }
                });
    }

    public interface GetDataCallBack {
        void onSuccess(List<TransactionQTUM> transactionQTUMList);
    }

    @Override
    public String getAddress() {
        return KeyStorage.getInstance(mContext).getWallet().currentReceiveAddress().toString();
    }
}
