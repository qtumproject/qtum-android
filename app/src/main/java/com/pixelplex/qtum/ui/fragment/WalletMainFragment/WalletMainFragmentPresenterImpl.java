package com.pixelplex.qtum.ui.fragment.WalletMainFragment;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class WalletMainFragmentPresenterImpl extends BaseFragmentPresenterImpl {

    private WalletMainFragmentInteractorImpl mWalletMainFragmentInteractor;
    private WalletMainFragmentView mWalletMainFragmentView;

    WalletMainFragmentPresenterImpl(WalletMainFragmentView walletFragmentView) {
        mWalletMainFragmentView = walletFragmentView;
        mWalletMainFragmentInteractor = new WalletMainFragmentInteractorImpl();
    }

    @Override
    public BaseFragmentView getView() {
        return mWalletMainFragmentView;
    }

    public void checkOtherTokens() {
        getTokens()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Contract>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Contract> tokens) {
                        mWalletMainFragmentView.showOtherTokens(tokens != null && tokens.size() > 0);
                    }
                });
    }

    private Observable<List<Contract>> getTokens() {
        return Observable.fromCallable(new Callable<List<Contract>>() {
            @Override
            public List<Contract> call() throws Exception {
                TinyDB tinyDB = new TinyDB(getView().getContext());
                List<Contract> contracts = tinyDB.getContractList();
                List<Contract> tokens = new ArrayList<>();

                for (Contract contract: contracts) {
                    if(contract.isToken()){
                        tokens.add(contract);
                    }
                }
                contracts.clear();
                return tokens;
            }
        });
    }
}
