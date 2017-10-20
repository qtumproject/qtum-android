package org.qtum.wallet.ui.fragment.qstore_by_type;

import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class QStoreByTypePresenterImpl extends BaseFragmentPresenterImpl implements QStoreByTypePresenter {

    private QStoreByTypeView view;
    private QStoreByTypeInteractor interactor;
    private int searchOffset;
    private String mType;

    public QStoreByTypePresenterImpl(QStoreByTypeView view, QStoreByTypeInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public QStoreByTypeView getView() {
        return view;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mType = getView().getType();
        getView().setUpTitle(mType);
        searchItems("", false);

    }

    @Override
    public void searchItems(String tag, boolean byTag) {
        searchOffset = 0;
        getInteractor().searchContractsObservable(searchOffset, mType, tag, byTag)
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

    public QStoreByTypeInteractor getInteractor() {
        return interactor;
    }
}
