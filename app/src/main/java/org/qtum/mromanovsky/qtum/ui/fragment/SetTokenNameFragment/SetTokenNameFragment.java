package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenNameFragment;


import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

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

    private final int LAYOUT = R.layout.fragment_set_token_name;
    private SetTokenNameFragmentPresenterImpl mSetTokenNameFragmentPresenter;

    public static SetTokenNameFragment newInstance(){
        SetTokenNameFragment setTokenNameFragment = new SetTokenNameFragment();
        return setTokenNameFragment;
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
        return LAYOUT;
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
}
