package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.content.Context;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClientImpl;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragmentInteractorImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PinFragmentInteractorImpl implements PinFragmentInteractor {

    private Context mContext;
    private QtumJSONRPCClientImpl mQtumJSONRPCClient;

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
    public void registerKey(String key, String identifier, final RegisterKeyCallBack callBack) {
        mQtumJSONRPCClient.registerKey(key, identifier)
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
                        QtumSharedPreference.getInstance().savePubKey(mContext, strings[0]);
                        callBack.onSuccess(strings);
                    }
                });
    }

    public interface RegisterKeyCallBack{
        void onSuccess(String[] keyAndIdentifier);
    }
}
