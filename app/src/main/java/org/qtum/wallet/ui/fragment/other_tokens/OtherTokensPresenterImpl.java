package org.qtum.wallet.ui.fragment.other_tokens;

import android.content.Context;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.token_fragment.TokenFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class OtherTokensPresenterImpl extends BaseFragmentPresenterImpl implements OtherTokensPresenter, UpdateSocketInstance {

    private Context mContext;
    private OtherTokensView view;
    private OtherTokensInteractorImpl interactor;

    public OtherTokensPresenterImpl(OtherTokensView view) {
        this.view = view;
        mContext = getView().getContext();
        this.interactor = new OtherTokensInteractorImpl();
    }

    public void openTokenDetails(Contract token) {
        BaseFragment tokenFragment = TokenFragment.newInstance(getView().getContext(), token);
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

                for (Token token : tokenList) {
                    if (token.isHasBeenCreated() && token.isSubscribe()) {
                        tokens.add(token);
                    }
                }
                tokenList.clear();
                return tokens;
            }
        });
    }

    public void notifyNewToken() {
        getTokens().subscribeOn(Schedulers.io())
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
                        if (tokens != null && tokens.size() > 0) {
                            getView().setTokensData(tokens);
                        }
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
                        if (tokens != null && tokens.size() > 0) {
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
