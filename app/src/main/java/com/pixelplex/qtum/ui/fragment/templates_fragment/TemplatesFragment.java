package com.pixelplex.qtum.ui.fragment.templates_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class TemplatesFragment extends BaseFragment implements TemplatesFragmentView, TemplateSelectListener {

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, TemplatesFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    private TemplatesFragmentPresenterImpl presenter;

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
    public void initializeViews() {
        super.initializeViews();
        contractList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void initializeRecyclerView(List<ContractTemplate> contractFullTemplateList, int resId){
        contractList.setAdapter(new TemplatesRecyclerAdapter(contractFullTemplateList,this, resId));
    }

    @Override
    public void onSelectContract(ContractTemplate contractTemplate) {
        presenter.openConstructorByName(contractTemplate.getUuid());
    }
}
