package com.pixelplex.qtum.ui.fragment.qstore.categories;


import com.pixelplex.qtum.model.gson.qstore.QstoreItem;

import java.util.List;

public class QstoreCategory {

    public String title;
    public List<QstoreItem> items;

    public QstoreCategory(String title, List<QstoreItem> items) {
        this.title = title;
        this.items = items;
    }

}
