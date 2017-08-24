package com.pixelplex.qtum.ui.fragment.QStore;

import com.pixelplex.qtum.model.gson.qstore.QSearchItem;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;
import com.pixelplex.qtum.ui.fragment.QStore.categories.QstoreCategory;

import java.util.List;

public interface QStoreView extends BaseFragmentView {
     void setCategories(List<QstoreCategory> categories);
     void setSearchResult(List<QSearchItem> items);
     void setSearchBarText(String text);
}
