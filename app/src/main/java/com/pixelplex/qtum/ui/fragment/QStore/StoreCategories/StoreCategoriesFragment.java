package com.pixelplex.qtum.ui.fragment.QStore.StoreCategories;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.SearchBar;
import com.pixelplex.qtum.utils.SearchBarListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 29.06.17.
 */

public class StoreCategoriesFragment extends BaseFragment implements StoreCategoriesView, SearchBarListener {

    StoreCategoriesPresenter presenter;

    StoreCategoriesAdapter adapter;

    @OnClick(R.id.ibt_back)
    public void onBackClick(){
        getActivity().onBackPressed();
    }

    @BindView(R.id.search_bar)
    SearchBar searchBar;

    @BindView(R.id.content_list)
    RecyclerView contentList;

    public static StoreCategoriesFragment newInstance() {
        Bundle args = new Bundle();
        StoreCategoriesFragment fragment = new StoreCategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
        contentList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StoreCategoriesAdapter(presenter.getSearchItems());
        contentList.setAdapter(adapter);
    }

    @Override
    protected void createPresenter() {
        presenter = new StoreCategoriesPresenter(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.lyt_store_categories;
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onDeactivate() {

    }

    @Override
    public void onRequestSearch(String filter) {
        adapter.updateItems(presenter.getFilter(filter));
    }
}
