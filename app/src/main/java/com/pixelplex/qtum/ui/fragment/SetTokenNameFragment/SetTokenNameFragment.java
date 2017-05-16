package com.pixelplex.qtum.ui.fragment.SetTokenNameFragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SetTokenNameFragment extends BaseFragment implements SetTokenNameFragmentView{

    @BindView(R.id.bt_cancel)
    Button mButtonCancel;

    @BindView(R.id.bt_next)
    Button mButtonNext;

    @BindView(R.id.et_token_name)
    TextInputEditText mTextInputEditTextTokenName;
    @BindView(R.id.til_token_name)
    TextInputLayout mTextInputLayoutTokenName;
    @BindView(R.id.et_token_symbol)
    TextInputEditText mTextInputEditTextTokenSymbol;
    @BindView(R.id.til_token_symbol)
    TextInputLayout mTextInputLayoutTokenSymbol;
    @BindView(R.id.rl_set_token_name)
    RelativeLayout mRelativeLayoutBase;

    @OnClick({R.id.bt_cancel,R.id.bt_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_cancel:
                getPresenter().onCancelClick();
                break;
            case R.id.bt_next:
                getPresenter().onNextClick(mTextInputEditTextTokenName.getText().toString(),
                        mTextInputEditTextTokenSymbol.getText().toString());
                break;
        }
    }

    private SetTokenNameFragmentPresenterImpl mSetTokenNameFragmentPresenter;

    public static SetTokenNameFragment newInstance() {

        Bundle args = new Bundle();

        SetTokenNameFragment fragment = new SetTokenNameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mSetTokenNameFragmentPresenter = new SetTokenNameFragmentPresenterImpl(this);
    }

    @Override
    protected SetTokenNameFragmentPresenterImpl getPresenter() {
        return mSetTokenNameFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_set_token_name;
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void setError(String nameError, String symbolError) {
        mTextInputLayoutTokenName.setError(nameError);
        mTextInputLayoutTokenSymbol.setError(symbolError);
    }

    @Override
    public void clearError() {
        mTextInputLayoutTokenName.setError("");
        mTextInputLayoutTokenSymbol.setError("");
    }

    @Override
    public void setData(String name, String symbol) {
        mTextInputEditTextTokenName.setText(name);
        mTextInputEditTextTokenSymbol.setText(symbol);
    }


    @Override
    public void initializeViews() {
        super.initializeViews();
        mRelativeLayoutBase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    hideKeyBoard();
            }
        });

        mTextInputEditTextTokenSymbol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    mRelativeLayoutBase.requestFocus();
                    return false;
                }
                return false;
            }
        });
    }

}