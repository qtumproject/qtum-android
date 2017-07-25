package com.pixelplex.qtum.ui.fragment.TemplateLibraryFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.TemplatesFragment.TemplateSelectListener;
import com.pixelplex.qtum.ui.fragment.TemplatesFragment.TemplatesRecyclerAdapter;
import com.pixelplex.qtum.ui.fragment.WatchContractFragment.WatchContractFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class TemplateLibraryFragment extends BaseFragment implements TemplateLibraryFragmentView{

    private static final String IS_TOKEN_LIBRARY = "is_token_library";

    @BindView(R.id.recycler_view)
    RecyclerView contractList;

    @OnClick(R.id.ibt_back)
    public void onClick() {
        getActivity().onBackPressed();
    }

    TemplateLibraryFragmentPresenter mTemplateLibraryFragmentPresenter;

    public static BaseFragment newInstance(Context context, boolean isTokenLibrary) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, TemplateLibraryFragment.class);
        args.putBoolean(IS_TOKEN_LIBRARY, isTokenLibrary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mTemplateLibraryFragmentPresenter = new TemplateLibraryFragmentPresenter(this);
    }

    @Override
    protected TemplateLibraryFragmentPresenter getPresenter() {
        return mTemplateLibraryFragmentPresenter;
    }

    @Override
    public boolean isTokenLibrary() {
        return getArguments().getBoolean(IS_TOKEN_LIBRARY);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        contractList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void initializeRecyclerView(List<ContractTemplate> contractFullTemplateList, int resId){
        contractList.setAdapter(new TemplatesRecyclerAdapter(contractFullTemplateList, new TemplateSelectListener() {
            @Override
            public void onSelectContract(ContractTemplate contractTemplate) {
                String abiInterface = FileStorageManager.getInstance().readAbiContract(getContext(),contractTemplate.getUuid());
                ((WatchContractFragment) getTargetFragment()).setABIInterface(abiInterface);
                getMainActivity().onBackPressed();
            }
        }, resId));
    }
}
