package com.pixelplex.qtum.ui.fragment.SetTokenParametersFragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SetTokenParametersFragment extends BaseFragment implements SetTokenParametersFragmentView{

    @BindView(R.id.bt_back)
    Button mButtonBack;
    @BindView(R.id.bt_finish)
    Button mButtonFinish;

    @BindView(R.id.et_initial_supply)
    TextInputEditText mTextInputEditTextInitialSupply;
    @BindView(R.id.til_initial_supply)
    TextInputLayout mTextInputLayoutInitialSupply;
    @BindView(R.id.et_decimal_units)
    TextInputEditText mTextInputEditTextDecimalUnits;
    @BindView(R.id.til_decimal_units)
    TextInputLayout mTextInputLayoutDecimalUnits;
    @BindView(R.id.rl_token_parameters)
    RelativeLayout mRelativeLayoutBase;

    @OnClick({R.id.bt_back,R.id.bt_finish})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_back:
                getPresenter().onBackClick();
                break;
            case R.id.bt_finish:
                getPresenter().onFinishClick(mTextInputEditTextInitialSupply.getText().toString(),
                        mTextInputEditTextDecimalUnits.getText().toString());
                break;
        }
    }

    private SetTokenParametersFragmentPresenterImpl mSetTokenParametersFragmentPresenter;

    public static SetTokenParametersFragment newInstance() {

        Bundle args = new Bundle();

        SetTokenParametersFragment fragment = new SetTokenParametersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mSetTokenParametersFragmentPresenter = new SetTokenParametersFragmentPresenterImpl(this);
    }

    @Override
    protected SetTokenParametersFragmentPresenterImpl getPresenter() {
        return mSetTokenParametersFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_set_token_parameters;
    }

    @Override
    public void setError(String supplyError, String unitsError) {
        mTextInputLayoutDecimalUnits.setError(unitsError);
        mTextInputLayoutInitialSupply.setError(supplyError);
    }

    @Override
    public void clearError() {
        mTextInputLayoutDecimalUnits.setError("");
        mTextInputLayoutInitialSupply.setError("");
    }

    @Override
    public void setData(String initialSupply, String decimalUnits) {
        if(!initialSupply.equals("0")) {
            mTextInputEditTextInitialSupply.setText(initialSupply);
        }
        if(!initialSupply.equals("0")) {
            mTextInputEditTextDecimalUnits.setText(decimalUnits);
        }
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
        mTextInputEditTextDecimalUnits.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
