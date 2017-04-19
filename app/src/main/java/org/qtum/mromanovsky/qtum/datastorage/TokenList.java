package org.qtum.mromanovsky.qtum.datastorage;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenParams;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenBalance.TokenBalance;

import java.util.ArrayList;
import java.util.List;

public class TokenList {
    private static TokenList sTokenList;

    private List<TokenParams> mList;

    private TokenList(){
        mList = new ArrayList<>();
    }

    public static TokenList getTokenList(){
        if(sTokenList == null){
            sTokenList = new TokenList();
        }
        return sTokenList;
    }

    public List<TokenParams> getList() {
        return mList;
    }

    public void addToTokenList(TokenParams tokenParams){
        mList.add(tokenParams);
    }

    public void setTokenBalance(TokenBalance tokenBalance){
        for(TokenParams tokenParams : mList){
            if(tokenParams.getAddress().equals(tokenBalance.getContractAddress())){
                tokenParams.setBalance(tokenBalance);
                return;
            }
        }
    }

}
