package org.qtum.mromanovsky.qtum.datastorage;


import com.google.common.collect.Lists;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryList {
    private static HistoryList sHistoryList;

    private List<History> mHistoryList;

    private HistoryList() {
        mHistoryList = new ArrayList<>();
    }

    public static HistoryList getInstance() {
        if (sHistoryList == null) {
            sHistoryList = new HistoryList();
        }
        return sHistoryList;
    }

    public List<History> getHistoryList() {
        return mHistoryList;
    }

    public void setHistoryList(List<History> historyList) {
        mHistoryList = historyList;
    }
}