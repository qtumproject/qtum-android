package org.qtum.wallet.ui.fragment.store_categories;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.QStoreCategoriesStorage;
import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.model.gson.qstore.QstoreItem;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.qstore.QStoreFragment;
import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class StoreCategoriesPresenter extends BaseFragmentPresenterImpl {

    private StoreCategoriesView view;

    public StoreCategoriesPresenter(StoreCategoriesView view) {
        this.view = view;
    }

    @Override
    public StoreCategoriesView getView() {
        return view;
    }

    private List<QstoreContractType> mQstoreContractTypes;
    private List<QstoreItem> mQstoreItems;

    @Override
    public void initializeViews() {
        super.initializeViews();
        setUpCategories();
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        QStoreCategoriesStorage.newInstance().addQStoreCategoriesStorageListener(new QStoreCategoriesStorage.QStoreCategoriesStorageListener() {
            @Override
            public void onQStoreCategoriesChange(List<QstoreCategory> qstoreCategories) {

            }
        });
    }


    @Override
    public void onPause(Context context) {
        super.onPause(context);
    }

    private void setUpCategories(){
        QtumService.newInstance().getContractTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<QstoreContractType>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<QstoreContractType> qstoreContractTypes) {
                        mQstoreContractTypes = qstoreContractTypes;
                        getView().setUpCategoriesList(qstoreContractTypes, new StoreCategoryViewHolder.OnCategoryClickListener() {
                            @Override
                            public void onClick(String type) {
                                BaseFragment qStroreFragment = QStoreFragment.newInstance(type, getView().getContext());
                                getView().openFragment(qStroreFragment);
                            }
                        });
                    }
                });
    }

    public List<QstoreContractType> getFilter(String filter) {
        List<QstoreContractType> filterList = new ArrayList<>();
        for (int i = 0; i < mQstoreContractTypes.size(); i ++){
            if(mQstoreContractTypes.get(i).getType().toLowerCase().contains(filter.toLowerCase())){
                filterList.add(mQstoreContractTypes.get(i));
            }
        }
        return filterList;
    }
}
