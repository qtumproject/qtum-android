package com.pixelplex.qtum.ui.fragment.qstore;

import com.pixelplex.qtum.model.gson.qstore.QSearchItem;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;
import com.pixelplex.qtum.ui.fragment.qstore.categories.QstoreCategory;

import java.util.List;

public interface QStoreView extends BaseFragmentView {
     void setCategories(List<QstoreCategory> categories);
     void setSearchResult(List<QSearchItem> items);
     void setSearchBarText(String text);
}
