package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import android.content.Context;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.List;


public class SubscribeTokensFragmentInteractor {

    Context mContext;

    public SubscribeTokensFragmentInteractor(Context context){
        mContext = context;
    }


    public List<Token> getTokenList() {
        List<Token> tokenList = (new TinyDB(mContext)).getTokenList();
        return tokenList;
    }


    public void saveTokenList(List<Token> tokenList) {
        (new TinyDB(mContext)).putTokenList(tokenList);
    }
}
