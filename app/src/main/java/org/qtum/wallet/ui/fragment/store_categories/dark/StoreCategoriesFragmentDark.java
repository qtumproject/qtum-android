package org.qtum.wallet.ui.fragment.store_categories.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesAdapter;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesFragment;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoryViewHolder;
import org.qtum.wallet.utils.SearchBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 18.08.17.
 */

public class StoreCategoriesFragmentDark extends StoreCategoriesFragment {

    @BindView(R.id.search_bar)
    SearchBar searchBar;

    @Override
    protected int getLayout() {
        return R.layout.lyt_store_categories;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);

    }

    @Override
    public void setUpCategoriesList(List<QstoreContractType> list, StoreCategoryViewHolder.OnCategoryClickListener listener) {
        adapter = new StoreCategoriesAdapter(list, R.layout.lyt_store_category_list_item, listener);
        contentList.setAdapter(adapter);
    }
}
