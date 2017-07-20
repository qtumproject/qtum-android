package com.pixelplex.qtum.ui.fragment.ImportWalletFragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.KeyStorage;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class ImportWalletFragmentInteractorImpl implements ImportWalletFragmentInteractor {

    private Context mContext;
    static boolean isDataLoaded = false;
    static String sPassphrase;

    ImportWalletFragmentInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public void importWallet(String seed, final ImportWalletCallBack callBack) {
        KeyStorage.getInstance()
                .importWallet(seed, mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String passphrase) {
                        isDataLoaded = true;
                        sPassphrase = passphrase;
                        callBack.onSuccess();
                    }
                });
    }

    interface ImportWalletCallBack {
        void onSuccess();
    }
}
