package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import android.content.Context;

import org.bitcoinj.wallet.Wallet;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class ImportWalletFragmentInteractorImpl implements ImportWalletFragmentInteractor {

    private Context mContext;
    static boolean isDataLoaded = false;

    ImportWalletFragmentInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public void importWallet(String seed, final ImportWalletCallBack callBack) {
        KeyStorage.getInstance()
                .importWallet(seed, mContext)
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
                        isDataLoaded = true;
                        callBack.onSuccess();
                    }
                });
    }

    interface ImportWalletCallBack {
        void onSuccess();
    }
}
