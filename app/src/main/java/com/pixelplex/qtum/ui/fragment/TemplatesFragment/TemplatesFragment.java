package com.pixelplex.qtum.ui.fragment.TemplatesFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class TemplatesFragment extends BaseFragment implements TemplatesFragmentView, TemplateSelectListener {

    public final int LAYOUT = R.layout.fragment_templates;

    public static TemplatesFragment newInstance() {
        Bundle args = new Bundle();
        TemplatesFragment fragment = new TemplatesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TemplatesFragmentPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView contractList;

    @OnClick(R.id.ibt_back)
    public void onClick() {
        getActivity().onBackPressed();
    }

    @Override
    protected void createPresenter() {
        presenter = new TemplatesFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        contractList.setLayoutManager(new LinearLayoutManager(getContext()));
        contractList.setAdapter(new TemplatesRecyclerAdapter(FileStorageManager.getInstance().getContractTemplateList(getContext()),this));
    }

    @Override
    public void onSelectContract(String contractName) {
        presenter.openConstructorByName(contractName);
    }
}
