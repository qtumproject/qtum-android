package org.qtum.wallet.ui.fragment.import_wallet_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.KeyStorage;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class ImportWalletInteractorImpl implements ImportWalletInteractor {

    private WeakReference<Context> mContext;

    ImportWalletInteractorImpl(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public Observable<String> importWallet(String seed) {
        return KeyStorage.getInstance().importWallet(seed);
    }
}
