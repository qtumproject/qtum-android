package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import android.content.Context;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.List;

class CurrencyFragmentInteractorImpl implements CurrencyFragmentInteractor{

    private Context mContext;

    public CurrencyFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public List<Token> getTokenList() {
        return (new TinyDB(mContext)).getTokenList();
    }

}
