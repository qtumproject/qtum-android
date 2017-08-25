package com.pixelplex.qtum.ui.fragment.qstore.dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.qstore.QSearchItem;
import com.pixelplex.qtum.ui.fragment.qstore.QStoreFragment;
import com.pixelplex.qtum.ui.fragment.qstore.StoreAdapter;
import com.pixelplex.qtum.ui.fragment.qstore.StoreSearchAdapter;
import com.pixelplex.qtum.ui.fragment.qstore.categories.QstoreCategory;
import com.pixelplex.qtum.utils.SearchBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 17.08.17.
 */

public class QStoreFragmentDark extends QStoreFragment {

    @BindView(R.id.search_bar)
    SearchBar searchBar;

    @Override
    protected int getLayout() {
        return R.layout.lyt_q_store;
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
        storeAdapter = new StoreAdapter(categories, this, R.layout.lyt_store_list_item, R.layout.lyt_store_token_list_item);
        contentList.setAdapter(storeAdapter);
    }


    @Override
    public void setSearchResult(List<QSearchItem> items) {
        searchAdapter = new StoreSearchAdapter(items, this, R.layout.lyt_store_search_list_item);
        contentList.setAdapter(searchAdapter);
    }
}
