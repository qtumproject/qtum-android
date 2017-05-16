package com.pixelplex.qtum.datastorage;




import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryList {
    private static HistoryList sHistoryList;

    private List<History> mHistoryList;
    private String mBalance;
    private String mUnconfirmedBalance;
    private int mTotalItem = 0;

    private HistoryList() {
        mHistoryList = new ArrayList<>();
    }

    public static HistoryList getInstance() {
        if (sHistoryList == null) {
            sHistoryList = new HistoryList();
        }
        return sHistoryList;
    }

    public void clearHistoryList() {
        sHistoryList = null;
    }

    public List<History> getHistoryList() {
        return mHistoryList;
    }

    public void setHistoryList(List<History> historyList) {
        mHistoryList = historyList;
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public int getTotalItem() {
        return mTotalItem;
    }

    public void setTotalItem(int totalItem) {
        this.mTotalItem = totalItem;
    }

    public String getUnconfirmedBalance() {
        return mUnconfirmedBalance;
    }

    public void setUnconfirmedBalance(String unconfirmedBalance) {
        mUnconfirmedBalance = unconfirmedBalance;
    }
}