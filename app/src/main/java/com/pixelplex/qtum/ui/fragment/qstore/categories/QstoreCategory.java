package com.pixelplex.qtum.ui.fragment.qstore.categories;


import com.pixelplex.qtum.model.gson.qstore.QstoreItem;

import java.util.List;

public class QstoreCategory {

    public String mTitle;
    public List<QstoreItem> mItems;

    public QstoreCategory(String title, List<QstoreItem> items) {
        this.mTitle = title;
        this.mItems = items;
    }

}
