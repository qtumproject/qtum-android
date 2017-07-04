package com.pixelplex.qtum.ui.fragment.WalletMainFragment;
import android.content.Context;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class WalletMainFragmentPresenterImpl extends BaseFragmentPresenterImpl {

    private WalletMainFragmentInteractorImpl mWalletMainFragmentInteractor;
    private WalletMainFragmentView mWalletMainFragmentView;

    WalletMainFragmentPresenterImpl(WalletMainFragmentView walletFragmentView) {
        mWalletMainFragmentView = walletFragmentView;
        mWalletMainFragmentInteractor = new WalletMainFragmentInteractorImpl();
    }

    @Override
    public WalletMainFragmentView getView() {
        return mWalletMainFragmentView;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        checkOtherTokens();
    }

    private void checkOtherTokens() {
        getTokens()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Token>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Token> tokens) {
                        getView().showOtherTokens(tokens != null && tokens.size() > 0);
                    }
                });
    }

    private Observable<List<Token>> getTokens() {
        return Observable.fromCallable(new Callable<List<Token>>() {
            @Override
            public List<Token> call() throws Exception {
                TinyDB tinyDB = new TinyDB(getView().getContext());
                List<Token> tokenList = tinyDB.getTokenList();
                List<Token> tokens = new ArrayList<>();

                for (Token token: tokenList) {
                    if(token.isSubscribe()){
                        tokens.add(token);
                    }
                }
                tokenList.clear();
                return tokens;
            }
        });
    }
}