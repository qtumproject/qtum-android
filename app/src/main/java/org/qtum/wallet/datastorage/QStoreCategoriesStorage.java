package org.qtum.wallet.datastorage;

import org.qtum.wallet.model.gson.qstore.QstoreItem;
import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maksimromanovskij on 27.09.17.
 */

public class QStoreCategoriesStorage {

    private static QStoreCategoriesStorage sQStoreCategoriesStorage;
    private List<QstoreCategory> mQstoreCategories = new ArrayList<>();
    private QStoreItemsStorageListener mQStoreItemsStorageListener;
    private List<QstoreItem> mQstoreItems = new ArrayList<>();

    private QStoreCategoriesStorage(){

    }

    public static QStoreCategoriesStorage newInstance(){
        if(sQStoreCategoriesStorage == null){
            return new QStoreCategoriesStorage();
        }
        return sQStoreCategoriesStorage;
    }

    public static QStoreCategoriesStorage getQStoreCategoriesStorage() {
        return sQStoreCategoriesStorage;
    }

    public static void setQStoreCategoriesStorage(QStoreCategoriesStorage QStoreCategoriesStorage) {
        sQStoreCategoriesStorage = QStoreCategoriesStorage;
    }

    public List<QstoreCategory> getQstoreItems() {
        return mQstoreCategories;
    }

    public void setQstoreItems(List<QstoreCategory> qstoreItems) {
        mQstoreCategories = qstoreItems;
    }

    public void addQStoreItemsStorageListener(QStoreItemsStorageListener qStoreItemsStorageListener){
        mQStoreItemsStorageListener = qStoreItemsStorageListener;
        qStoreItemsStorageListener.onQStoreItemsChange(mQstoreItems);
    }

    public void addCategoryToQStoreCategories(QstoreCategory qstoreCategory){
        mQstoreCategories.add(qstoreCategory);
        mQstoreItems.addAll(qstoreCategory.mItems);
        if(mQStoreItemsStorageListener !=null){
            mQStoreItemsStorageListener.onQStoreItemsChange(mQstoreItems);
        }
    }

    public void removeQStoreItemsStorageListener(){
        mQStoreItemsStorageListener = null;
    }

    public interface QStoreItemsStorageListener {
        void onQStoreItemsChange(List<QstoreItem> qstoreItems);
    }
}
