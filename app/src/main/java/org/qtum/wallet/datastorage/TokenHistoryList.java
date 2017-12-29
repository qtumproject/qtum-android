package org.qtum.wallet.datastorage;


import org.qtum.wallet.model.gson.token_history.TokenHistory;

import java.util.ArrayList;
import java.util.List;

public class TokenHistoryList {

    private static TokenHistoryList sTokenHistoryList;

    private List<TokenHistory> mTokenHistories;
    private int totalItems;

    private TokenHistoryList(){
        mTokenHistories = new ArrayList<>();
    }

    public static TokenHistoryList newInstance(){
        if(sTokenHistoryList==null){
            sTokenHistoryList = new TokenHistoryList();
        }
        return sTokenHistoryList;
    }

    public void clearTokenHistory(){
        sTokenHistoryList = null;
    }

    public List<TokenHistory> getTokenHistories() {
        return mTokenHistories;
    }

    public void setTokenHistories(List<TokenHistory> tokenHistories) {
        mTokenHistories = tokenHistories;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
