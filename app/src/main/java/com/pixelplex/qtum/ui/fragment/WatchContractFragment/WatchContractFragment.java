package com.pixelplex.qtum.ui.fragment.WatchContractFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontButton;
import com.pixelplex.qtum.utils.FontManager;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class WatchContractFragment extends BaseFragment implements WatchContractFragmentView {

    private static final String IS_TOKEN = "is_token";

    private WatchContractFragmentPresenter mWatchContractFragmentPresenter;

    private boolean mIsToken;

    @BindView(R.id.et_contract_name) protected TextInputEditText mEditTextContractName;
    @BindView(R.id.et_contract_address) protected TextInputEditText mEditTextContractAddress;
    @BindView(R.id.et_abi_interface) protected EditText mEditTextABIInterface;
    @BindView(R.id.tv_toolbar_watch) protected FontTextView mTextViewToolbar;
    @BindView(R.id.rv_templates) protected RecyclerView mRecyclerViewTemplates;
    @BindView(R.id.til_contract_name) protected TextInputLayout mTilContractName;
    @BindView(R.id.til_contract_address) protected TextInputLayout mTilContractAddress;

    @BindView(R.id.bt_ok)
    FontButton mButtonConfirm;

    private boolean isEmptyContractName = true;
    private boolean isEmptyContractAddress = true;
    private boolean isEmptyABIInterface = true;

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

    public static BaseFragment newInstance(Context context, boolean isToken) {
        Bundle args = new Bundle();
        args.putBoolean(IS_TOKEN,isToken);
        BaseFragment fragment = Factory.instantiateFragment(context, WatchContractFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mIsToken = getArguments().getBoolean(IS_TOKEN);
        if(mIsToken){
            mTilContractName.setHint(getResources().getString(R.string.token_name));
            mTilContractAddress.setHint(getResources().getString(R.string.token_address));
            mTextViewToolbar.setText(getString(R.string.watch_token));
        } else {
            mTextViewToolbar.setText(getString(R.string.watch_contract));
        }

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .build();
        mRecyclerViewTemplates.setLayoutManager(chipsLayoutManager);

        mEditTextABIInterface.setHorizontallyScrolling(false);
        mEditTextABIInterface.setMaxLines(Integer.MAX_VALUE);

        mEditTextABIInterface.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEmptyABIInterface = editable.toString().isEmpty();
                checkForNoEmpty(isEmptyABIInterface,isEmptyContractAddress,isEmptyContractName);
            }
        });

        mEditTextContractAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEmptyContractAddress = editable.toString().isEmpty();
                checkForNoEmpty(isEmptyABIInterface,isEmptyContractAddress,isEmptyContractName);
            }
        });

        mEditTextContractName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEmptyContractName = editable.toString().isEmpty();
                checkForNoEmpty(isEmptyABIInterface,isEmptyContractAddress,isEmptyContractName);
            }
        });

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

    private void checkForNoEmpty(boolean ... isEmptyParams){
        for(boolean isEmpty : isEmptyParams){
            if(isEmpty) {
                mButtonConfirm.setEnabled(false);
                return;
            }
        }
        mButtonConfirm.setEnabled(true);
    }

    @Override
    public void notifyAdapter() {
        mRecyclerViewTemplates.getAdapter().notifyDataSetChanged();
    }

}