package org.qtum.wallet.datastorage;

import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maksimromanovskij on 27.09.17.
 */

public class QStoreCategoriesStorage {

    private static QStoreCategoriesStorage sQStoreCategoriesStorage;
    private List<QstoreCategory> mQstoreCategories = new ArrayList<>();
    private QStoreCategoriesStorageListener mQStoreCategoriesStorageListener;

    private QStoreCategoriesStorage(){

    }

    public static QStoreCategoriesStorage newInstance(){
        if(sQStoreCategoriesStorage == null){
            sQStoreCategoriesStorage = new QStoreCategoriesStorage();
        }
        return sQStoreCategoriesStorage;
    }

    public static QStoreCategoriesStorage getQStoreCategoriesStorage() {
        return sQStoreCategoriesStorage;
    }

    public static void setQStoreCategoriesStorage(QStoreCategoriesStorage QStoreCategoriesStorage) {
        sQStoreCategoriesStorage = QStoreCategoriesStorage;
    }

    public List<QstoreCategory> getQstoreCategories() {
        return mQstoreCategories;
    }

    public void setQstoreItems(List<QstoreCategory> qstoreItems) {
        mQstoreCategories = qstoreItems;
    }

    public void addQStoreCategoriesStorageListener(QStoreCategoriesStorageListener qStoreCategoriesStorageListener){
        mQStoreCategoriesStorageListener = qStoreCategoriesStorageListener;
        qStoreCategoriesStorageListener.onQStoreCategoriesChange(mQstoreCategories);
    }

    public void addCategoryToQStoreCategories(QstoreCategory qstoreCategory){
        mQstoreCategories.add(qstoreCategory);
        if(mQStoreCategoriesStorageListener !=null){
            mQStoreCategoriesStorageListener.onQStoreCategoriesChange(mQstoreCategories);
        }
    }

    public void removeQStoreCategoriesStorageListener(){
        mQStoreCategoriesStorageListener = null;
    }

    public interface QStoreCategoriesStorageListener {
        void onQStoreCategoriesChange(List<QstoreCategory> qstoreCategories);
    }
}
