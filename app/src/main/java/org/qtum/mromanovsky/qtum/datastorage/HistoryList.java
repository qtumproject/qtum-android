package org.qtum.mromanovsky.qtum.datastorage;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryList {
    private static HistoryList sHistoryList;

    private List<History> mHistoryList;
    private long mBalance = 0;

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

    public long getBalance() {
        return mBalance;
    }

    public void setBalance(long balance) {
        mBalance = balance;
    }
}