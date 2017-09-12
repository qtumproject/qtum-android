package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment;

import android.content.Context;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.datastorage.TinyDB;

import java.util.List;


public class SubscribeTokensFragmentInteractor {

    private Context mContext;

    public SubscribeTokensFragmentInteractor(Context context){
        mContext = context;
    }


    public List<Token> getTokenList() {
        return (new TinyDB(mContext)).getTokenList();
    }


    public void saveTokenList(List<Token> tokenList) {
        (new TinyDB(mContext)).putTokenList(tokenList);
    }
}
