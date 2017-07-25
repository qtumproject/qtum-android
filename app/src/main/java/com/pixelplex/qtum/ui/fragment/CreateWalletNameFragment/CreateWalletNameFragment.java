package com.pixelplex.qtum.ui.fragment.CreateWalletNameFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class CreateWalletNameFragment extends BaseFragment implements CreateWalletNameFragmentView {

    private static final String IS_CREATE_NEW = "is_create_new";
    private static final String PASSPHRASE = "passphrase";
    public static boolean mIsCreateNew;

    private CreateWalletNameFragmentPresenterImpl mCreateWalletFragmentPresenter;

    @BindView(R.id.bt_confirm)
    Button mButtonConfirm;
    @BindView(R.id.bt_cancel)
    Button mButtonCancel;
    @BindView(R.id.et_wallet_name)
    TextInputEditText mTextInputEditTextWalletName;
    @BindView(R.id.til_wallet_name)
    TextInputLayout mTextInputLayoutWalletName;

    @OnClick({R.id.bt_confirm, R.id.bt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                getPresenter().onConfirmClick(mTextInputEditTextWalletName.getText().toString());
                break;
            case R.id.bt_cancel:
                getPresenter().onCancelClick();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsCreateNew = getArguments().getBoolean(IS_CREATE_NEW);
    }

    public static BaseFragment newInstance(Context context, boolean isCreateNew, String passphrase) {
        BaseFragment createWalletNameFragment = Factory.instantiateFragment(context, CreateWalletNameFragment.class);
        Bundle args = new Bundle();
        args.putBoolean(IS_CREATE_NEW, isCreateNew);
        args.putString(PASSPHRASE,passphrase);
        createWalletNameFragment.setArguments(args);
        return createWalletNameFragment;
    }

    public static BaseFragment newInstance(Context context, boolean isCreateNew) {
        BaseFragment createWalletNameFragment = Factory.instantiateFragment(context, CreateWalletNameFragment.class);
        Bundle args = new Bundle();
        args.putBoolean(IS_CREATE_NEW, isCreateNew);
        createWalletNameFragment.setArguments(args);
        return createWalletNameFragment;
    }

    @Override
    protected void createPresenter() {
        mCreateWalletFragmentPresenter = new CreateWalletNameFragmentPresenterImpl(this);
    }

    @Override
    protected CreateWalletNameFragmentPresenterImpl getPresenter() {
        return mCreateWalletFragmentPresenter;
    }

    @Override
    public void initializeViews() {
        setFocusTextInput(mTextInputLayoutWalletName,mTextInputEditTextWalletName);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void setErrorText(String errorText) {
        mTextInputEditTextWalletName.setText("");
        mTextInputLayoutWalletName.setErrorEnabled(true);
        mTextInputLayoutWalletName.setError(errorText);
    }

    @Override
    public void clearError() {
        mTextInputLayoutWalletName.setErrorEnabled(false);
    }

    @Override
    public String getPassphrase() {
        return getArguments().getString(PASSPHRASE);
    }

    @Override
    public boolean isCreating() {
        return getArguments().getBoolean(IS_CREATE_NEW);
    }

}
