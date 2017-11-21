package org.qtum.wallet.ui.fragment.store_categories;

import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface StoreCategoriesView extends BaseFragmentView {
    void setUpCategoriesList(List<QstoreContractType> list, StoreCategoryViewHolder.OnCategoryClickListener listener);
}
