package org.qtum.wallet.datastorage;

import org.qtum.wallet.model.gson.history.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryList {
    private static HistoryList sHistoryList;

    private List<History> mHistoryList;
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

    public int getTotalItem() {
        return mTotalItem;
    }

    public void setTotalItem(int totalItem) {
        this.mTotalItem = totalItem;
    }

}