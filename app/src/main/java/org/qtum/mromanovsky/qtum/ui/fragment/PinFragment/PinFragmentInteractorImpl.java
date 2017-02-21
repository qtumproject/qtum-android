package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.content.Context;

import org.bitcoinj.wallet.Wallet;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class PinFragmentInteractorImpl implements PinFragmentInteractor {

    private Context mContext;
    static boolean isDataLoaded = false;

    PinFragmentInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public int getPassword() {
        return QtumSharedPreference.getInstance().getWalletPassword(mContext);
    }

    @Override
    public void savePassword(int password) {
        QtumSharedPreference.getInstance().saveWalletPassword(mContext, password);
    }



    @Override
    public void loadWalletFromFile(final LoadWalletFromFileCallBack callBack) {
        KeyStorage.getInstance()
                .loadWalletFromFile(mContext)
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

    @Override
    public void createWallet(Context context, final CreateWalletCallBack callBack) {
        KeyStorage.getInstance()
                .createWallet(mContext)
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
                        setKeyGeneratedInstance(true);
                        isDataLoaded = true;
                        callBack.onSuccess();
                    }
                });
    }

    @Override
    public void setKeyGeneratedInstance(boolean isKeyGenerated) {
        QtumSharedPreference.getInstance().setKeyGeneratedInstance(mContext, isKeyGenerated);
    }


    interface CreateWalletCallBack {
        void onSuccess();
    }

    interface LoadWalletFromFileCallBack {
        void onSuccess();
    }

}
