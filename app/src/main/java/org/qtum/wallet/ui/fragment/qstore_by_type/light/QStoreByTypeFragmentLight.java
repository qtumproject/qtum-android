package org.qtum.wallet.ui.fragment.qstore_by_type.light;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.fragment.qstore_by_type.QStoreByTypeFragment;
import org.qtum.wallet.ui.fragment.qstore_by_type.StoreSearchAdapter;

import java.util.List;

/**
 * Created by kirillvolkov on 17.08.17.
 */

public class QStoreByTypeFragmentLight extends QStoreByTypeFragment {

    @Override
    protected int getLayout() {
        return R.layout.lyt_q_store_light;
    }

    @Override
    public void setSearchResult(List<QSearchItem> items) {
        searchAdapter = new StoreSearchAdapter(items, this, R.layout.lyt_store_search_list_item_light);
        contentList.setAdapter(searchAdapter);
    }
}
