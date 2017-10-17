package org.qtum.wallet.ui.fragment.other_tokens;


import android.content.Context;

import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Token;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

public class OtherTokensInteractorImpl implements OtherTokensInteractor {

    private WeakReference<Context> mContext;

    public OtherTokensInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public Observable<List<Token>> getTokenObservable() {
        return Observable.fromCallable(new Callable<List<Token>>() {
            @Override
            public List<Token> call() throws Exception {
                TinyDB tinyDB = new TinyDB(mContext.get());
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
}
