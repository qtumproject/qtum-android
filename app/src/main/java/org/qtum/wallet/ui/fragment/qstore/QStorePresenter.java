package org.qtum.wallet.ui.fragment.qstore;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.QStoreCategoriesStorage;
import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.model.gson.qstore.QstoreItem;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class QStorePresenter extends BaseFragmentPresenterImpl {

    private QStoreView view;
    private List<QstoreCategory> categories;
    private int searchOffset;

    public QStorePresenter(QStoreView view){
        this.view = view;
    }

    @Override
    public QStoreView getView() {
        return view;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        categories = new ArrayList<>();
        loadCategories();
    }

    public void loadCategories(){
        QtumService.newInstance()
                .getTrendingNow()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QstoreItem>>() {
                    @Override
                    public void onCompleted() {
                        loadWatsNew();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadWatsNew();
                    }

                    @Override
                    public void onNext(List<QstoreItem> qstoreItems) {
                        QstoreCategory qstoreCategory = new QstoreCategory(getView().getContext().getString(R.string.trending_now),qstoreItems);
                        categories.add(qstoreCategory);
                        QStoreCategoriesStorage.newInstance().addCategoryToQStoreCategories(qstoreCategory);
                        getView().setCategories(categories);

                    }
                });
    }

    public void loadWatsNew(){
        QtumService.newInstance()
                .getWatsNew()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QstoreItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<QstoreItem> qstoreItems) {
                        QstoreCategory qstoreCategory = new QstoreCategory(getView().getContext().getString(R.string.whats_new),qstoreItems);
                        categories.add(qstoreCategory);
                        QStoreCategoriesStorage.newInstance().addCategoryToQStoreCategories(qstoreCategory);
                        getView().setCategories(categories);
                    }
                });
    }

    public void searchItems(String tag, boolean byTag){
        searchOffset = 0;
        QtumService.newInstance()
                .searchContracts(searchOffset,tag, byTag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QSearchItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<QSearchItem> qstoreItems) {
                        getView().setSearchResult(qstoreItems);
                    }
                });
    }
}
