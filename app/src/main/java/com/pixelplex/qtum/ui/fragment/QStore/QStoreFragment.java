package com.pixelplex.qtum.ui.fragment.QStore;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.store.QSearchItem;
import com.pixelplex.qtum.model.gson.store.QstoreItem;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.QStore.StoreCategories.StoreCategoriesFragment;
import com.pixelplex.qtum.ui.fragment.QStore.StoreContract.StoreContractFragment;
import com.pixelplex.qtum.ui.fragment.QStore.categories.QstoreCategory;
import com.pixelplex.qtum.utils.FontCheckBox;
import com.pixelplex.qtum.utils.SearchBar;
import com.pixelplex.qtum.utils.SearchBarListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class QStoreFragment extends BaseFragment implements QStoreView, SearchBarListener, StoreItemClickListener{

    private QStorePresenter presenter;

    private StoreAdapter storeAdapter;
    private StoreSearchAdapter searchAdapter;

    @OnClick(R.id.ibt_back)
    public void onBackClick(){
        getActivity().onBackPressed();
    }

    @BindView(R.id.search_bar)
    SearchBar searchBar;

    @BindView(R.id.content_list)
    RecyclerView contentList;

    @BindView(R.id.search_type_menu)
    LinearLayout searchTypeMenu;

    @BindView(R.id.cb_by_name)
    FontCheckBox cbByName;

    @BindView(R.id.cb_by_tag)
    FontCheckBox cbByTag;

    @OnClick(R.id.ibt_categories)
    public void onCategoriesClick() {
        openFragment(StoreCategoriesFragment.newInstance());
    }

    public static QStoreFragment newInstance() {
        Bundle args = new Bundle();
        QStoreFragment fragment = new QStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        presenter = new QStorePresenter(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
        contentList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hideBottomNavView(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        showBottomNavView(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.lyt_q_store;
    }

    @Override
    public void onActivate() {
        if(searchTypeMenu != null) {
            searchTypeMenu.setVisibility(View.VISIBLE);
        }
        contentList.setAdapter(searchAdapter);
    }

    @Override
    public void onDeactivate() {
        if(searchTypeMenu != null) {
            searchTypeMenu.setVisibility(View.GONE);
        }
        if(contentList != null && storeAdapter != null) {
            contentList.setAdapter(storeAdapter);
        }
    }

    @Override
    public void onRequestSearch(String filter) {
        if(!TextUtils.isEmpty(filter)) {
            presenter.searchItemsByTag(filter);
        }
    }

    @Override
    public void OnItemClick(QstoreItem item) {
        openFragmentForResult(this, StoreContractFragment.newInstance(item.id));
    }

    public void setSearchTag(String tag){
        cbByName.setChecked(false);
        cbByTag.setChecked(true);
        searchBar.setText(tag);
    }

    @Override
    public void setCategories(List<QstoreCategory> categories) {
        storeAdapter = new StoreAdapter(categories, this);
        contentList.setAdapter(storeAdapter);
    }

    @Override
    public void setSearchResult(List<QSearchItem> items) {
        searchAdapter = new StoreSearchAdapter(items, this);
        contentList.setAdapter(searchAdapter);
    }
}
