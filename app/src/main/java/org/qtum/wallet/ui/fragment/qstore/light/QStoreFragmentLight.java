package org.qtum.wallet.ui.fragment.qstore.light;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.fragment.qstore.QStoreFragment;
import org.qtum.wallet.ui.fragment.qstore.StoreAdapter;
import org.qtum.wallet.ui.fragment.qstore.StoreSearchAdapter;
import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.List;


public class QStoreFragmentLight extends QStoreFragment {

    @Override
    protected int getLayout() {
        return R.layout.lyt_q_store_light;
    }

    @Override
    public void setCategories(List<QstoreCategory> categories) {
        storeAdapter = new StoreAdapter(categories, this, R.layout.lyt_store_list_item_light, R.layout.lyt_store_token_list_item_light);
        contentList.setAdapter(storeAdapter);
    }


    @Override
    public void setSearchResult(List<QSearchItem> items) {
        searchAdapter = new StoreSearchAdapter(items, this, R.layout.lyt_store_search_list_item_light);
        contentList.setAdapter(searchAdapter);
    }
}
