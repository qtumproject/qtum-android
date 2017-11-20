package org.qtum.wallet.ui.fragment.qstore.categories;

import org.qtum.wallet.model.gson.qstore.QstoreItem;

import java.util.List;

public class QstoreCategory {

    private String mTitle;
    private List<QstoreItem> mItems;

    public QstoreCategory(String title, List<QstoreItem> items) {
        this.mTitle = title;
        this.mItems = items;
    }

    public QstoreCategory(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<QstoreItem> getItems() {
        return mItems;
    }

    public void setItems(List<QstoreItem> items) {
        mItems = items;
    }
}
