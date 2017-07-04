package com.pixelplex.qtum.ui.fragment.PinFragment;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;


public class PinFragment extends BaseFragment implements PinFragmentView {

    private PinFragmentPresenterImpl mPinFragmentPresenter;

    private final static String ACTION = "action";

    public final static String CREATING = "creating";
    public final static String CHECK_AUTHENTICATION = "check_authentication";
    public final static String AUTHENTICATION = "authentication";
    public final static String AUTHENTICATION_AND_SEND = "authentication_and_send";
    public final static String CHANGING = "changing";
    public final static String IMPORTING = "importing";

    @BindView(R.id.til_wallet_pin)
    PinEntryEditText mWalletPin;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_title)
    FontTextView mTextViewToolBarTitle;

    @BindView(R.id.tooltip)
    FontTextView tooltip;

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
        if(mWalletPin!=null) {
            mWalletPin.setText("");
            tooltip.setText(errorText);
            tooltip.setTextColor(ContextCompat.getColor(getContext(),R.color.accent_red_color));
            tooltip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateState(String state) {
        mWalletPin.setText("");
        tooltip.setText(state);
        tooltip.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        tooltip.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearError() {
        tooltip.setText("");
        tooltip.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setToolBarTitle(int titleID) {
        mTextViewToolBarTitle.setText(titleID);
    }

    @Override
    public void setPin(String pin) {
        mWalletPin.setText(pin);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void initializeViews() {
        getPresenter().setAction(getArguments().getString(ACTION));

        mWalletPin.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str.length() == 4) {
                    getPresenter().confirm(str.toString());
                }
            }
        });

        mWalletPin.setFocusableInTouchMode(true);
        mWalletPin.requestFocus();
        showSoftInput();

    }
}
