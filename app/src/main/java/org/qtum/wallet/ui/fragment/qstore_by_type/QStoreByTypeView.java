package org.qtum.wallet.ui.fragment.qstore_by_type;

import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;
import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.List;

public interface QStoreByTypeView extends BaseFragmentView {
     void setSearchResult(List<QSearchItem> items);
     void setSearchBarText(String text);
     String getSeacrhBarText();
     String getType();
     void setUpTitle(String type);
}
