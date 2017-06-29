package com.pixelplex.qtum.ui.fragment.QStore.StoreCategories;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.QStore.TestTokenObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirillvolkov on 29.06.17.
 */

public class StoreCategoriesPresenter extends BaseFragmentPresenterImpl {

    StoreCategoriesView view;

    public StoreCategoriesPresenter(StoreCategoriesView view) {
        this.view = view;
    }

    @Override
    public StoreCategoriesView getView() {
        return view;
    }

    List<TestTokenObject> items;

    public List<TestTokenObject> getSearchItems(){
        items = new ArrayList<>();
        for (int i = 0; i < 30; i ++){
            items.add(new TestTokenObject());
        }
        return items;
    }

    public List<TestTokenObject> getFilter(String filter) {
        List<TestTokenObject> filterList = new ArrayList<>();
        for (int i = 0; i < items.size(); i ++){
            if(items.get(i).name.toLowerCase().contains(filter.toLowerCase())){
                filterList.add(items.get(i));
            }
        }
        return filterList;
    }
}
