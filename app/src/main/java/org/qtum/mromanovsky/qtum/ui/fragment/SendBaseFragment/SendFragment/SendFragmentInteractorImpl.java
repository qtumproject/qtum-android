package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.btc.BTCUtils;
import org.qtum.mromanovsky.qtum.btc.Transaction;
import org.qtum.mromanovsky.qtum.btc.UnspentOutputInfo;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClientImpl;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SendFragmentInteractorImpl implements SendFragmentInteractor {

    private Context mContext;
    QtumJSONRPCClientImpl mQtumJSONRPCClient = new QtumJSONRPCClientImpl();

    public SendFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public void createTx(final String address, final String amount, final CreateTxCallBack callBack) {
        mQtumJSONRPCClient.getUnspentOutputInfo(QtumSharedPreference.getInstance().getAddress(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<UnspentOutputInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArrayList<UnspentOutputInfo> unspentOutputInfos) {
                        //Transaction transaction = BTCUtils.createTransaction(unspentOutputInfos,address,,amount,1,,);
                        //callBack.onSuccess(transaction);
                    }
                });
    }

    @Override
    public void sendTx(Transaction transaction, final SendTxCallBack callBack) {
        String txHash = BTCUtils.toHex(transaction.getBytes());
        mQtumJSONRPCClient.sendTx(txHash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        callBack.onSuccess();
                    }
                });
    }

    public interface CreateTxCallBack {
        void onSuccess(Transaction transaction);
    }

    public interface SendTxCallBack {
        void onSuccess();
    }
}