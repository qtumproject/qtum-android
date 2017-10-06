package org.qtum.wallet.ui.fragment.store_categories;

import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class StoreCategoriesPresenterImpl extends BaseFragmentPresenterImpl implements StoreCategoriesPresenter {

    private StoreCategoriesView view;
    private StoreCategoriesInteractor interactor;
    private List<QstoreContractType> mQstoreContractTypes;

    public StoreCategoriesPresenterImpl(StoreCategoriesView view, StoreCategoriesInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public StoreCategoriesView getView() {
        return view;
    }

    @Override
    public Observable<List<QstoreContractType>> categoriesObservable() {
        return getInteractor().contractTypesObservable();
    }

    public List<QstoreContractType> getFilter(String filter) {
        List<QstoreContractType> filterList = new ArrayList<>();
        for (int i = 0; i < mQstoreContractTypes.size(); i++) {
            if (mQstoreContractTypes.get(i).getType().toLowerCase().contains(filter.toLowerCase())) {
                filterList.add(mQstoreContractTypes.get(i));
            }
        }
        return filterList;
    }

    public StoreCategoriesInteractor getInteractor() {
        return interactor;
    }

    @Override
    public void setContractTypes(List<QstoreContractType> types) {
        mQstoreContractTypes = types;
    }
}
