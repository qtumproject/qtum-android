package org.qtum.wallet.ui.fragment.import_wallet_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.KeyStorage;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import rx.Observable;

class ImportWalletInteractorImpl implements ImportWalletInteractor {

    private WeakReference<Context> mContext;

    ImportWalletInteractorImpl(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public Observable<String> importWallet(final String seed) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return KeyStorage.getInstance().loadWallet(seed);
            }
        });
    }
}
