package org.qtum.mromanovsky.qtum.ui.fragment.CurrencyFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenParams;
import org.qtum.mromanovsky.qtum.datastorage.TokenList;

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
