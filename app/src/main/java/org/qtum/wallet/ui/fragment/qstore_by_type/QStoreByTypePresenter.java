package org.qtum.wallet.ui.fragment.qstore_by_type;


import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class QStoreByTypePresenter extends BaseFragmentPresenterImpl {

    private QStoreByTypeView view;
    private int searchOffset;
    private String mType;

    public QStoreByTypePresenter(QStoreByTypeView view){
        this.view = view;
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
        searchItems("",false);

    }

    public void searchItems(String tag, boolean byTag){
        searchOffset = 0;
        QtumService.newInstance()
                .searchContracts(searchOffset, mType,tag, byTag)
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
