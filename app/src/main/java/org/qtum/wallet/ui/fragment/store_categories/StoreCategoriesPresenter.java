package org.qtum.wallet.ui.fragment.store_categories;

import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import java.util.List;

import rx.Observable;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public interface StoreCategoriesPresenter extends BaseFragmentPresenter {
    List<QstoreContractType> getFilter(String filter);

    Observable<List<QstoreContractType>> categoriesObservable();

    void setContractTypes(List<QstoreContractType> types);

}
