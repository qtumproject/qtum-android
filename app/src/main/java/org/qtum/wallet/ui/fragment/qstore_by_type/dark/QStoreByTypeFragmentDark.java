package org.qtum.wallet.ui.fragment.qstore_by_type.dark;

import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.fragment.qstore_by_type.QStoreByTypeFragment;
import org.qtum.wallet.ui.fragment.qstore_by_type.StoreSearchAdapter;
import org.qtum.wallet.utils.SearchBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 17.08.17.
 */

public class QStoreByTypeFragmentDark extends QStoreByTypeFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.lyt_q_store;
    }

    @Override
    public void setSearchResult(List<QSearchItem> items) {
        searchAdapter = new StoreSearchAdapter(items, this, org.qtum.wallet.R.layout.lyt_store_search_list_item);
        contentList.setAdapter(searchAdapter);
    }
}
