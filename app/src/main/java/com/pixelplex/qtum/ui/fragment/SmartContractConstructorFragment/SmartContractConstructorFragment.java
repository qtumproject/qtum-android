package com.pixelplex.qtum.ui.fragment.SmartContractConstructorFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class SmartContractConstructorFragment extends BaseFragment implements SmartContractConstructorView {

    public final int LAYOUT = R.layout.lyt_smart_contract_constructor;
    public final static String CONTRACT_TEMPLATE_NAME = "contract_template_name";

    private ConstructorAdapter adapter;

    public static SmartContractConstructorFragment newInstance(String contractTemplateName) {
        Bundle args = new Bundle();
        SmartContractConstructorFragment fragment = new SmartContractConstructorFragment();
        args.putString(CONTRACT_TEMPLATE_NAME,contractTemplateName);
        fragment.setArguments(args);
        return fragment;
    }

    SmartContractConstructorPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView constructorList;

    @BindView(R.id.tv_template_name)
    FontTextView mTextViewTemplateName;

    @OnClick({R.id.ibt_back, R.id.cancel})
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.confirm)
    public void onConfirmClick(){
        if(adapter != null) {
           // adapter.notifyDataSetChanged();
            presenter.confirm(adapter.getParams(), getArguments().getString(CONTRACT_TEMPLATE_NAME));
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SmartContractConstructorPresenterImpl(this);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        constructorList.setLayoutManager(new LinearLayoutManager(getContext()));
        String templateName = getArguments().getString(CONTRACT_TEMPLATE_NAME);
        presenter.getConstructorByName(templateName);
        mTextViewTemplateName.setText(templateName);
    }

    @Override
    public void onResume() {
        hideBottomNavView(false);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        showBottomNavView(false);
    }

    @Override
    public void onContractConstructorPrepared(List<ContractMethodParameter> params) {
        adapter = new ConstructorAdapter(params);
        constructorList.setAdapter(adapter);
    }
}
