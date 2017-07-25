package com.pixelplex.qtum.ui.fragment.WatchContractFragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontButton;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class WatchContractFragment extends BaseFragment implements WatchContractFragmentView {

    private static final String IS_TOKEN = "is_token";

    private WatchContractFragmentPresenter mWatchContractFragmentPresenter;

    private boolean mIsToken;

    @BindView(R.id.et_contract_name)
    TextInputEditText mEditTextContractName;
    @BindView(R.id.et_contract_address)
    TextInputEditText mEditTextContractAddress;
    @BindView(R.id.et_abi_interface)
    EditText mEditTextABIInterface;
    @BindView(R.id.tv_toolbar_watch)
    FontTextView mTextViewToolbar;
    @BindView(R.id.rv_templates)
    RecyclerView mRecyclerViewTemplates;

    @BindView(R.id.bt_choose_from_library)
    FontButton mFontButtonChooseFromLibrary;

    @OnClick({R.id.ibt_back,R.id.bt_ok,R.id.bt_cancel,R.id.bt_choose_from_library})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.bt_cancel:
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.bt_ok:
                String name = mEditTextContractName.getText().toString();
                String address = mEditTextContractAddress.getText().toString();
                String jsonInterface = mEditTextABIInterface.getText().toString();
                getPresenter().onOkClick(name,address,jsonInterface, mIsToken);
                break;
            case R.id.bt_choose_from_library:
                getPresenter().onChooseFromLibraryClick(mIsToken);
                break;
        }
    }

    public static WatchContractFragment newInstance(boolean isToken) {
        
        Bundle args = new Bundle();
        args.putBoolean(IS_TOKEN,isToken);
        WatchContractFragment fragment = new WatchContractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mIsToken = getArguments().getBoolean(IS_TOKEN);
        if(mIsToken){
            mTextViewToolbar.setText(getString(R.string.watch_token));
        } else {
            mTextViewToolbar.setText(getString(R.string.watch_contract));
        }

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .build();
        mRecyclerViewTemplates.setLayoutManager(chipsLayoutManager);
    }

    @Override
    protected void createPresenter() {
        mWatchContractFragmentPresenter = new WatchContractFragmentPresenter(this);
    }

    @Override
    protected WatchContractFragmentPresenter getPresenter() {
        return mWatchContractFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_watch_contract;
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
    public void setABIInterface(String abiInterface) {
        mEditTextABIInterface.setText(abiInterface);
    }

    @Override
    public boolean isToken() {
        return getArguments().getBoolean(IS_TOKEN);
    }

    @Override
    public void setUpTemplatesList(List<ContractTemplate> contractTemplateList, OnTemplateClickListener listener) {
        mRecyclerViewTemplates.setAdapter(new TemplatesAdapter(contractTemplateList, listener));
    }
}
