package org.qtum.wallet.ui.fragment.store_categories.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesAdapter;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesFragment;
import org.qtum.wallet.utils.SearchBar;

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
        adapter = new StoreCategoriesAdapter(getPresenter().getSearchItems(), R.layout.lyt_store_category_list_item);
        contentList.setAdapter(adapter);
    }
}
