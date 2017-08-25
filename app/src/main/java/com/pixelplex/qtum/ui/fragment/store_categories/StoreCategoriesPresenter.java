package com.pixelplex.qtum.ui.fragment.store_categories;

import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.qstore.TestTokenObject;

import java.util.ArrayList;
import java.util.List;


public class StoreCategoriesPresenter extends BaseFragmentPresenterImpl {

    private StoreCategoriesView view;

    public StoreCategoriesPresenter(StoreCategoriesView view) {
        this.view = view;
    }

    @Override
    public StoreCategoriesView getView() {
        return view;
    }

    private List<TestTokenObject> items;

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
