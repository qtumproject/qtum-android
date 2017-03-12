package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class PinFragment extends BaseFragment implements PinFragmentView {

    public final int LAYOUT = R.layout.fragment_pin;

    PinFragmentPresenterImpl mPinFragmentPresenter;

    public final static String CREATING = "creating";
    public final static String AUTHENTICATION = "authentication";
    public final static String CHANGING = "changing";
    public final static String IMPORTING = "importing";

    public final static String ENTER_PIN = "enter pin";
    public final static String ENTER_NEW_PIN = "enter new pin";
    public final static String REPEAT_PIN = "repeat pin";
    public final static String ENTER_OLD_PIN = "enter old pin";

    public final static String[] CREATING_STATE = new String[]{ENTER_NEW_PIN,REPEAT_PIN};
    public final static String[] AUTHENTICATION_STATE = new String[]{ENTER_PIN};
    public final static String[] CHANGING_STATE = new String[]{ENTER_OLD_PIN,ENTER_NEW_PIN,REPEAT_PIN};
    public static int currentState = 0;

    final static String ACTION = "action";
    public static String sAction;


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
        return LAYOUT;
    }


    @Override
    public void confirmError(String errorText) {
        mTextInputEditTextWalletPin.setText("");
        mTextInputLayoutWalletPin.setError(errorText);
    }

    @Override
    public void updateState() {
        mTextInputEditTextWalletPin.setText("");
        switch (sAction) {
            case IMPORTING:
            case CREATING:
                mTextInputLayoutWalletPin.setHint(CREATING_STATE[currentState]);
                break;
            case AUTHENTICATION:
                mTextInputLayoutWalletPin.setHint(AUTHENTICATION_STATE[currentState]);
                break;
            case CHANGING:
                mTextInputLayoutWalletPin.setHint(CHANGING_STATE[currentState]);
                break;
        }
    }

    @Override
    public void clearError() {
        mTextInputLayoutWalletPin.setError("");
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void initializeViews() {
        sAction = getArguments().getString(ACTION);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
            switch (sAction) {
                case IMPORTING:
                case CREATING:
                    mTextViewToolBarTitle.setText(R.string.create_pin);
                    break;
                case AUTHENTICATION:
                    mTextViewToolBarTitle.setText(R.string.enter_pin);
                    break;
                case CHANGING:
                    //actionBar.setDisplayHomeAsUpEnabled(true);
                    mTextViewToolBarTitle.setText(R.string.change_pin);
                    break;
            }

            mTextInputEditTextWalletPin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString().length()==4){
                        getPresenter().confirm(editable.toString());
                    }
                }
            });

        }
    }
}
