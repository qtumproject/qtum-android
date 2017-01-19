package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class PinFragment extends BaseFragment implements PinFragmentView {

    public static final int LAYOUT = R.layout.fragment_pin;

    PinFragmentPresenterImpl mPinFragmentPresenter;

    public final static String CREATING = "creating";
    public final static String AUTHENTICATION = "authentication";
    public final static String CHANGING = "changing";

    final static String ACTION = "action";
    private String mAction;

    @BindView(R.id.bt_confirm)
    Button mButtonConfirm;
    @BindView(R.id.bt_cancel)
    Button mButtonCancel;
    @BindView(R.id.et_wallet_pin)
    TextInputEditText mTextInputEditTextWalletPin;
    @BindView(R.id.til_wallet_pin)
    TextInputLayout mTextInputLayoutWalletPin;
    @BindView(R.id.et_wallet_new_pin)
    TextInputEditText mTextInputEditTextWalletNewPin;
    @BindView(R.id.til_wallet_new_pin)
    TextInputLayout mTextInputLayoutWalletNewPin;
    @BindView(R.id.et_wallet_new_pin_repeat)
    TextInputEditText mTextInputEditTextWalletNewPinRepeat;
    @BindView(R.id.til_wallet_new_pin_repeat)
    TextInputLayout mTextInputLayoutWalletNewPinRepeat;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView mTextViewToolBarTitle;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @OnClick({R.id.bt_confirm, R.id.bt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                String[] passwords = new String[3];
                passwords[0] = mTextInputEditTextWalletPin.getText().toString();
                passwords[1] = mTextInputEditTextWalletNewPin.getText().toString();
                passwords[2] = mTextInputEditTextWalletNewPinRepeat.getText().toString();
                getPresenter().confirm(passwords, mAction);
                break;
            case R.id.bt_cancel:
                getPresenter().cancel(mAction);
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
        return LAYOUT;
    }


    @Override
    public void confirmError(String errorText) {
        mTextInputEditTextWalletPin.setText("");
        mTextInputLayoutWalletPin.setErrorEnabled(true);
        mTextInputLayoutWalletPin.setError(errorText);
    }

    @Override
    public void confirmChangePinError(String errorTextNewPin, String errorTextRepeatPin) {
        mTextInputEditTextWalletNewPin.setText("");
        mTextInputLayoutWalletNewPin.setErrorEnabled(true);
        mTextInputLayoutWalletNewPin.setError(errorTextNewPin);

        mTextInputEditTextWalletNewPinRepeat.setText("");
        mTextInputLayoutWalletNewPinRepeat.setErrorEnabled(true);
        mTextInputLayoutWalletNewPinRepeat.setError(errorTextRepeatPin);

        mTextInputLayoutWalletNewPin.requestFocus();

    }

    @Override
    public void setProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearErrors() {
        mTextInputLayoutWalletPin.setErrorEnabled(false);
        mTextInputLayoutWalletNewPin.setErrorEnabled(false);
        mTextInputLayoutWalletNewPinRepeat.setErrorEnabled(false);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void initializeViews() {
        mAction = getArguments().getString(ACTION);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            switch (mAction) {
                case CREATING:
                    mTextViewToolBarTitle.setText(R.string.create_pin);
                    break;
                case AUTHENTICATION:
                    mTextViewToolBarTitle.setText(R.string.enter_pin);
                    break;
                case CHANGING:
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    mTextViewToolBarTitle.setText(R.string.change_pin);
                    mTextInputLayoutWalletNewPin.setVisibility(View.VISIBLE);
                    mTextInputLayoutWalletNewPinRepeat.setVisibility(View.VISIBLE);
                    break;
            }

//            mTextInputEditTextWalletPin.setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                    if (i == KeyEvent.KEYCODE_ENTER) {
//                        mTextInputLayoutWalletPin.clearFocus();
//                        mTextInputEditTextWalletPin.clearFocus();
//                        hideKeyBoard(mTextInputEditTextWalletPin);
//                        Log.d("MyLog", "onKey");
//                        return true;
//                    }
//                    return false;
//                }
//            });
        }
    }
}
