package com.pixelplex.qtum.ui.fragment.OtherTokens;

import android.content.Context;

import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.TokenFragment.TokenFragment;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class OtherTokensPresenterImpl extends BaseFragmentPresenterImpl implements OtherTokensPresenter, UpdateSocketInstance {

    private Context mContext;
    OtherTokensView view;
    OtherTokensInteractorImpl interactor;

    public OtherTokensPresenterImpl (OtherTokensView view) {
        this.view = view;
        mContext = getView().getContext();
        this.interactor = new OtherTokensInteractorImpl();
    }

    public void openTokenDetails(Contract token) {
        TokenFragment tokenFragment = TokenFragment.newInstance(token);
        getView().openFragment(tokenFragment);
    }

    @Override
    public OtherTokensView getView() {
        return view;
    }

    private Observable<List<Token>> getTokens() {
        return Observable.fromCallable(new Callable<List<Token>>() {
            @Override
            public List<Token> call() throws Exception {
                TinyDB tinyDB = new TinyDB(mContext);
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

    public void setTokenList() {
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
                if(tokens != null && tokens.size() > 0) {
                    getView().setTokensData(tokens);
                }
            }
        });
    }

    @Override
    public UpdateService getSocketInstance() {
        return getView().getMainActivity().getUpdateService();
    }
}
