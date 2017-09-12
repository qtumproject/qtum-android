package org.qtum.wallet.ui.fragment.watch_contract_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;

import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontButton;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class WatchContractFragment extends BaseFragment implements WatchContractFragmentView {

    private static final String IS_TOKEN = "is_token";

    private WatchContractFragmentPresenter mWatchContractFragmentPresenter;

    private boolean mIsToken;

    @BindView(org.qtum.wallet.R.id.et_contract_name) protected TextInputEditText mEditTextContractName;
    @BindView(org.qtum.wallet.R.id.et_contract_address) protected TextInputEditText mEditTextContractAddress;
    @BindView(org.qtum.wallet.R.id.et_abi_interface) protected EditText mEditTextABIInterface;
    @BindView(org.qtum.wallet.R.id.tv_toolbar_watch) protected FontTextView mTextViewToolbar;
    @BindView(org.qtum.wallet.R.id.rv_templates) protected RecyclerView mRecyclerViewTemplates;
    @BindView(org.qtum.wallet.R.id.til_contract_name) protected TextInputLayout mTilContractName;
    @BindView(org.qtum.wallet.R.id.til_contract_address) protected TextInputLayout mTilContractAddress;

    @BindView(org.qtum.wallet.R.id.bt_ok)
    FontButton mButtonConfirm;

    private boolean isEmptyContractName = true;
    private boolean isEmptyContractAddress = true;
    private boolean isEmptyABIInterface = true;

    @BindView(org.qtum.wallet.R.id.bt_choose_from_library)
    FontButton mFontButtonChooseFromLibrary;

    @OnClick({org.qtum.wallet.R.id.ibt_back, org.qtum.wallet.R.id.bt_ok, org.qtum.wallet.R.id.bt_cancel, org.qtum.wallet.R.id.bt_choose_from_library})
    public void onClick(View view){
        switch (view.getId()) {
            case org.qtum.wallet.R.id.bt_cancel:
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case org.qtum.wallet.R.id.bt_ok:
                String name = mEditTextContractName.getText().toString();
                String address = mEditTextContractAddress.getText().toString();
                String jsonInterface = mEditTextABIInterface.getText().toString();
                getPresenter().onOkClick(name,address,jsonInterface, mIsToken);
                break;
            case org.qtum.wallet.R.id.bt_choose_from_library:
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
            mTilContractName.setHint(getResources().getString(org.qtum.wallet.R.string.token_name));
            mTilContractAddress.setHint(getResources().getString(org.qtum.wallet.R.string.token_address));
            mTextViewToolbar.setText(getString(org.qtum.wallet.R.string.watch_token));
        } else {
            mTextViewToolbar.setText(getString(org.qtum.wallet.R.string.watch_contract));
        }

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .build();
        mRecyclerViewTemplates.setLayoutManager(chipsLayoutManager);

        mEditTextABIInterface.setHorizontallyScrolling(false);
        mEditTextABIInterface.setMaxLines(Integer.MAX_VALUE);

        mEditTextABIInterface.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                isEmptyABIInterface = editable.toString().isEmpty();
                checkForNoEmpty(isEmptyABIInterface,isEmptyContractAddress,isEmptyContractName);
            }
        });

        mEditTextContractAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                isEmptyContractAddress = editable.toString().isEmpty();
                checkForNoEmpty(isEmptyABIInterface,isEmptyContractAddress,isEmptyContractName);
            }
        });

        mEditTextContractName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
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
    public void setABIInterface(String name, String abiInterface) {
        mEditTextABIInterface.setText(abiInterface);
    }

    public void setABIInterfaceForResult(String name, String abiInterface){
        mEditTextABIInterface.setText(abiInterface);
        ((TemplatesAdapter)mRecyclerViewTemplates.getAdapter()).setSelection(name);
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
    public void notifyAdapter(int adapterPosition) {
        ((TemplatesAdapter)mRecyclerViewTemplates.getAdapter()).setSelection(adapterPosition);
    }

}