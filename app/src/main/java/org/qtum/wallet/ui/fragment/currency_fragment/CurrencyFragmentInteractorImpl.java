package org.qtum.wallet.ui.fragment.currency_fragment;


import android.content.Context;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.datastorage.TinyDB;

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
