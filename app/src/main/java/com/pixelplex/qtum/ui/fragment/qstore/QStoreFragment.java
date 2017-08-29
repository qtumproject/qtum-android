package com.pixelplex.qtum.ui.fragment.qstore;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.qstore.QstoreItem;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.store_categories.StoreCategoriesFragment;
import com.pixelplex.qtum.ui.fragment.store_contract.StoreContractFragment;
import com.pixelplex.qtum.utils.FontCheckBox;
import com.pixelplex.qtum.utils.SearchBarListener;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class QStoreFragment extends BaseFragment implements QStoreView, SearchBarListener, StoreItemClickListener{

    private QStorePresenter presenter;

    protected StoreAdapter storeAdapter;
    protected StoreSearchAdapter searchAdapter;

    @OnClick(R.id.ibt_back)
    public void onBackClick(){
        getActivity().onBackPressed();
    }

    @BindView(R.id.content_list)
    protected
    RecyclerView contentList;

    @BindView(R.id.search_type_menu)
    LinearLayout searchTypeMenu;

    @BindView(R.id.cb_by_name)
    FontCheckBox cbByName;

    @BindView(R.id.cb_by_tag)
    FontCheckBox cbByTag;

    @OnClick(R.id.ibt_categories)
    public void onCategoriesClick() {
        openFragment(StoreCategoriesFragment.newInstance(getContext()));
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, QStoreFragment.class);
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
        openFragmentForResult(StoreContractFragment.newInstance(getContext(), item.id));
    }

    public void setSearchTag(String tag){
        cbByName.setChecked(false);
        cbByTag.setChecked(true);
        setSearchBarText(tag);
    }

}
