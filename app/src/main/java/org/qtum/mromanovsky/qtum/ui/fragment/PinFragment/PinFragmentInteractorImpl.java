package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClient;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClientImpl;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.utils.QtumCryptoGenerator;
import org.qtum.mromanovsky.qtum.utils.QtumCryptoGeneratorImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PinFragmentInteractorImpl implements PinFragmentInteractor {

    private Context mContext;
    private QtumJSONRPCClient mQtumJSONRPCClient;
    private QtumCryptoGenerator mQtumCryptoGenerator;

    public PinFragmentInteractorImpl(Context context) {
        mContext = context;
        mQtumJSONRPCClient = new QtumJSONRPCClientImpl();
        mQtumCryptoGenerator = new QtumCryptoGeneratorImpl();
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
    public void generateRegisterKeyAndID(final generateRegisterKeyAndIdentifierCallBack callBack) {
        mQtumJSONRPCClient
                .generateRegisterKeyAndIdentifier()
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
                        QtumSharedPreference.getInstance().saveIdentifier(mContext,strings[1]);
                        callBack.onSuccess(strings);
                    }
                });
    }


    public interface generateRegisterKeyAndIdentifierCallBack {
        void onSuccess(String[] keyAndIdentifier);
    }

}
