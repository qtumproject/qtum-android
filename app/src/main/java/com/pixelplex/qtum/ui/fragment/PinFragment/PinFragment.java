package com.pixelplex.qtum.ui.fragment.PinFragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class PinFragment extends BaseFragment implements PinFragmentView {

    private PinFragmentPresenterImpl mPinFragmentPresenter;

    final static String ACTION = "action";

    public final static String CREATING = "creating";
    public final static String AUTHENTICATION = "authentication";
    public final static String AUTHENTICATION_AND_SEND = "authentication_and_send";
    public final static String CHANGING = "changing";
    public final static String IMPORTING = "importing";

    @BindView(R.id.bt_cancel)
    Button mButtonCancel;
    @BindView(R.id.et_wallet_pin)
    TextInputEditText mTextInputEditTextWalletPin;
    @BindView(R.id.til_wallet_pin)
    TextInputLayout mTextInputLayoutWalletPin;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView mTextViewToolBarTitle;

    @OnClick({R.id.bt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                getPresenter().cancel();
                break;
        }
    }

    public static PinFragment newInstance(String action) {
        PinFragment pinFragment = new PinFragment();
        Bundle args = new Bundle();
        args.putString(ACTION, action);
        pinFragment.setArguments(args);
        return pinFragment;
    }

    @Override
    protected void createPresenter() {
        mPinFragmentPresenter = new PinFragmentPresenterImpl(this);
    }

    @Override
    protected PinFragmentPresenterImpl getPresenter() {
        return mPinFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_pin;
    }


    @Override
    public void confirmError(String errorText) {
        mTextInputEditTextWalletPin.setText("");
        mTextInputLayoutWalletPin.setError(errorText);
    }

    @Override
    public void updateState(String state) {
        mTextInputEditTextWalletPin.setText("");
        mTextInputLayoutWalletPin.setHint(state);
    }

    @Override
    public void clearError() {
        mTextInputLayoutWalletPin.setError("");
    }

    @Override
    public void setToolBarTitle(int titleID) {
        mTextViewToolBarTitle.setText(titleID);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void initializeViews() {
        getPresenter().setAction(getArguments().getString(ACTION));

        mTextInputEditTextWalletPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 4) {
                    getPresenter().confirm(editable.toString());
                }
            }
        });

        mTextInputEditTextWalletPin.setFocusableInTouchMode(true);
        mTextInputEditTextWalletPin.requestFocus();
        showSoftInput();

    }
}
