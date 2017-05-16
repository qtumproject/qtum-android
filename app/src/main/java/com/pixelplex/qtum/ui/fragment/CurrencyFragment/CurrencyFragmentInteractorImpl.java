package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.TokenParams;
import com.pixelplex.qtum.datastorage.TokenList;

import java.util.ArrayList;
import java.util.List;

class CurrencyFragmentInteractorImpl implements CurrencyFragmentInteractor{

    public CurrencyFragmentInteractorImpl(){

    }

    @Override
    public List<String> getTokenList() {
        List<String> tokenList = new ArrayList<>();
        for(TokenParams tokenParams : TokenList.getTokenList().getList()){
            tokenList.add(tokenParams.getName());
        }
        return tokenList;
    }
}
