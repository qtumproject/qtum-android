package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment;

import android.content.Context;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.datastorage.TinyDB;

import java.util.List;


public class SubscribeTokensInteractorImpl implements SubscribeTokensInteractor {

    private Context mContext;

    public SubscribeTokensInteractorImpl(Context context) {
        mContext = context;
    }


    @Override
    public List<Token> getTokenList() {
        return (new TinyDB(mContext)).getTokenList();
    }


    @Override
    public void saveTokenList(List<Token> tokenList) {
        (new TinyDB(mContext)).putTokenList(tokenList);
    }
}
