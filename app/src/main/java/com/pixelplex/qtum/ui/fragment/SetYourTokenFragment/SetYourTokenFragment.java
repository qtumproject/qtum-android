package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.Button;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FontEditText;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class SetYourTokenFragment extends BaseFragment implements SetYourTokenFragmentView, OnValidateParamsListener {

    private final int LAYOUT = R.layout.fragment_set_your_token;
    private final static String CONTRACT_TEMPLATE_UIID = "uiid";

    private ConstructorAdapter adapter;

    public static SetYourTokenFragment newInstance(String uiid) {
        Bundle args = new Bundle();
        SetYourTokenFragment fragment = new SetYourTokenFragment();
        args.putString(CONTRACT_TEMPLATE_UIID,uiid);
        fragment.setArguments(args);
        return fragment;
    }

    private SetYourTokenFragmentPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView constructorList;

    @BindView(R.id.tv_template_name)
    FontEditText mTextViewTemplateName;

    @OnClick({R.id.ibt_back, R.id.cancel})
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @BindView(R.id.confirm)
    Button confirmBtn;

    @OnClick(R.id.confirm)
    public void onConfirmClick(){
        if(adapter != null) {
            String name = mTextViewTemplateName.getText().toString();
           if(adapter.validateMethods() && !name.isEmpty()) {
               presenter.confirm(adapter.getParams(), getArguments().getString(CONTRACT_TEMPLATE_UIID), name);
           }
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SetYourTokenFragmentPresenterImpl(this);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        String templateUiid = getArguments().getString(CONTRACT_TEMPLATE_UIID);
        presenter.getConstructorByUiid(templateUiid);
        String templateName = "";
        TinyDB tinyDB = new TinyDB(getContext());
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
        for(ContractTemplate contractTemplate : contractTemplateList){
            if(contractTemplate.getUuid().equals(templateUiid)) {
                templateName = contractTemplate.getName();
                break;
            }
        }

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
        adapter = new ConstructorAdapter(params, this);
        constructorList.setAdapter(adapter);
    }

    @Override
    public void onValidate() {
        confirmBtn.setEnabled(adapter.validateMethods());
    }
}
