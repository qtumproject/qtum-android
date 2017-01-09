package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class PinFragment extends BaseFragment implements PinFragmentView {

    public static final int  LAYOUT = R.layout.fragment_pin;

    PinFragmentPresenterImpl mPinFragmentPresenter;
    final static String IS_CREATING = "is_creating";
    boolean mIsCreating;

    @BindView(R.id.bt_confirm) Button mButtonConfirm;
    @BindView(R.id.bt_cancel) Button mButtonCancel;
    @BindView(R.id.et_wallet_pin) TextInputEditText mTextInputEditTextWalletPin;
    @BindView(R.id.til_wallet_pin) TextInputLayout mTextInputLayoutWalletPin;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @OnClick({R.id.bt_confirm,R.id.bt_cancel})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.bt_confirm:
                getPresenter().confirm(mTextInputEditTextWalletPin.getText().toString(), mIsCreating);
                break;
            case R.id.bt_cancel:
                getPresenter().cancel();
                break;
        }
    }

    public static PinFragment newInstance(boolean isCreating){
        PinFragment pinFragment = new PinFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_CREATING, isCreating);
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
    public void confirm() {
        WalletFragment walletFragment = WalletFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, walletFragment, walletFragment.getClass().getCanonicalName())
                .commit();
    }

    @Override
    public void confirmError(String errorText) {
        mTextInputEditTextWalletPin.setText("");
        mTextInputLayoutWalletPin.setErrorEnabled(true);
        mTextInputLayoutWalletPin.setError(errorText);
    }

    @Override
    public void initializeViews() {
        mIsCreating = getArguments().getBoolean(IS_CREATING);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            if(mIsCreating) {
                actionBar.setTitle(R.string.create_pin);
            } else {
                actionBar.setTitle(R.string.enter_pin);
            }
            //actionBar.setDisplayHomeAsUpEnabled(true);

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
