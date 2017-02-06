package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import android.content.Context;

import org.bitcoinj.wallet.Wallet;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by max-v on 2/6/2017.
 */

public class ImportWalletFragmentInteractorImpl implements ImportWalletFragmentInteractor {

    Context mContext;

    public ImportWalletFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public void importWallet(String seed, final ImportWalletCallBack callBack) {
        KeyStorage.getInstance(mContext)
                .importWallet(seed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Wallet>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Wallet wallet) {
                        callBack.onSuccess();
                    }
                });
    }

    public interface ImportWalletCallBack{
        void onSuccess();
    }
}
