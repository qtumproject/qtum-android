package org.qtum.wallet.ui.fragment.store_categories.light;

import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesAdapter;
import org.qtum.wallet.ui.fragment.store_categories.StoreCategoriesFragment;
import org.qtum.wallet.utils.SearchBarLight;

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
        adapter = new StoreCategoriesAdapter(getPresenter().getSearchItems(), org.qtum.wallet.R.layout.lyt_store_category_list_item_light);
        contentList.setAdapter(adapter);
    }

}
