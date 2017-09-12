package org.qtum.wallet.ui.fragment.qstore.categories;


import org.qtum.wallet.model.gson.qstore.QstoreItem;

import java.util.List;

public class QstoreCategory {

    public String mTitle;
    public List<QstoreItem> mItems;

    public QstoreCategory(String title, List<QstoreItem> items) {
        this.mTitle = title;
        this.mItems = items;
    }

}
