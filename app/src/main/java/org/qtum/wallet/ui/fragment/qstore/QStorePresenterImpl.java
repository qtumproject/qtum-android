package org.qtum.wallet.ui.fragment.qstore;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.model.gson.qstore.QstoreItem;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class QStorePresenterImpl extends BaseFragmentPresenterImpl implements QStorePresenter {

    private QStoreView view;
    private QStoreInteractor interactor;
    private List<QstoreCategory> categories;
    private int searchOffset;
    private final String EMPTY_TYPE = "";

    public QStorePresenterImpl(QStoreView view, QStoreInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
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

    private void loadCategories() {
        getInteractor().getTrendingNowObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<QstoreItem>, Boolean>() {
                    @Override
                    public Boolean call(List<QstoreItem> qstoreItems) {
                        if (!qstoreItems.isEmpty()) {
                            QstoreCategory qstoreCategory = new QstoreCategory(getInteractor().getTrendingString(), qstoreItems);
                            categories.add(qstoreCategory);
                            getView().setCategories(categories);
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Boolean, Observable<List<QstoreItem>>>() {
                    @Override
                    public Observable<List<QstoreItem>> call(Boolean aBoolean) {
                        return getInteractor().getWhatsNewObservable();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<QstoreItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<QstoreItem> qstoreItems) {
                        if (!qstoreItems.isEmpty()) {
                            QstoreCategory qstoreCategory = new QstoreCategory(getInteractor().getWhatsNewString(), qstoreItems);
                            categories.add(qstoreCategory);
                            getView().setCategories(categories);
                        }

                    }
                });
    }

    @Override
    public void searchItems(String tag, boolean byTag) {
        searchOffset = 0;
        getInteractor().searchContracts(searchOffset, EMPTY_TYPE, tag, byTag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QSearchItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<QSearchItem> qstoreItems) {
                        getView().setSearchResult(qstoreItems);
                    }
                });
    }

    public QStoreInteractor getInteractor() {
        return interactor;
    }

    /**
     * Getter for unit testing
     */
    public List<QstoreCategory> getCategories() {
        return categories;
    }
}
