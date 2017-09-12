package org.qtum.wallet.ui.fragment.qstore.dark;

import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.fragment.qstore.QStoreFragment;
import org.qtum.wallet.ui.fragment.qstore.StoreAdapter;
import org.qtum.wallet.ui.fragment.qstore.StoreSearchAdapter;
import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;
import org.qtum.wallet.utils.SearchBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 17.08.17.
 */

public class QStoreFragmentDark extends QStoreFragment {

    @BindView(org.qtum.wallet.R.id.search_bar)
    SearchBar searchBar;

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.lyt_q_store;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
    }

    @Override
    public void setSearchBarText(String text) {
        searchBar.setText(text);
    }

    @Override
    public void setCategories(List<QstoreCategory> categories) {
        storeAdapter = new StoreAdapter(categories, this, org.qtum.wallet.R.layout.lyt_store_list_item, org.qtum.wallet.R.layout.lyt_store_token_list_item);
        contentList.setAdapter(storeAdapter);
    }


    @Override
    public void setSearchResult(List<QSearchItem> items) {
        searchAdapter = new StoreSearchAdapter(items, this, org.qtum.wallet.R.layout.lyt_store_search_list_item);
        contentList.setAdapter(searchAdapter);
    }
}
