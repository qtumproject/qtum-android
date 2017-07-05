package com.pixelplex.qtum.ui.fragment.QStore;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;


public class QStorePresenter extends BaseFragmentPresenterImpl {

    private QStoreView view;

    public QStorePresenter(QStoreView view){
        this.view = view;
    }

    @Override
    public QStoreView getView() {
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
