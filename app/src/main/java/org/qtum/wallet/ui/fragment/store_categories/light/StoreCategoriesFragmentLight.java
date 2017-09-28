package org.qtum.wallet.ui.fragment.store_categories.light;

import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesAdapter;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesFragment;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoryViewHolder;
import org.qtum.wallet.utils.SearchBarLight;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 18.08.17.
 */

public class StoreCategoriesFragmentLight extends StoreCategoriesFragment {

    @BindView(org.qtum.wallet.R.id.search_bar)
    SearchBarLight searchBar;

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.lyt_store_categories_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
    }

    @Override
    public void setUpCategoriesList(List<QstoreContractType> list, StoreCategoryViewHolder.OnCategoryClickListener listener) {
        adapter = new StoreCategoriesAdapter(list, org.qtum.wallet.R.layout.lyt_store_category_list_item_light, listener);
        contentList.setAdapter(adapter);
    }
}
