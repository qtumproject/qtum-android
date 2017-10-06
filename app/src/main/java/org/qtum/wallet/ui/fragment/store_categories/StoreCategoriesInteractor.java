package org.qtum.wallet.ui.fragment.store_categories;

import org.qtum.wallet.model.gson.QstoreContractType;

import java.util.List;

import rx.Observable;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public interface StoreCategoriesInteractor {
    Observable<List<QstoreContractType>> contractTypesObservable();
}
