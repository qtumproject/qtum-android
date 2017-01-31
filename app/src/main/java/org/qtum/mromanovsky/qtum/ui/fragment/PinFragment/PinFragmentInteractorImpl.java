package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.content.Context;

import org.bitcoinj.wallet.Wallet;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClient;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClientImpl;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PinFragmentInteractorImpl implements PinFragmentInteractor {

    private Context mContext;
    private QtumJSONRPCClient mQtumJSONRPCClient;

    public PinFragmentInteractorImpl(Context context) {
        mContext = context;
        mQtumJSONRPCClient = new QtumJSONRPCClientImpl();
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
    public void getWalletFromFile(final GetWalletFromFileCallBack callBack) {
        KeyStorage.getInstance(mContext).
                loadWalletFromFile()
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
                        callBack.onSuccess(wallet);
                    }
                });
    }

    @Override
    public void generateRegisterKeyAndID(Context context, final GenerateRegisterKeyAndIdentifierCallBack callBack) {
        mQtumJSONRPCClient
                .generateRegisterKeyAndIdentifier(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        QtumSharedPreference.getInstance().setKeyGeneratedInstance(mContext, true);
                        QtumSharedPreference.getInstance().saveIdentifier(mContext,strings[1]);
                        callBack.onSuccess(strings);
                    }
                });
    }


    public interface GenerateRegisterKeyAndIdentifierCallBack {
        void onSuccess(String[] keyAndIdentifier);
    }

    public interface GetWalletFromFileCallBack {
        void onSuccess(Wallet wallet);
    }

}
