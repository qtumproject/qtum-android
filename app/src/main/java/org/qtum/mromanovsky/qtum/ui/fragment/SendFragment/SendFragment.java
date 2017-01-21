package org.qtum.mromanovsky.qtum.ui.fragment.SendFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import butterknife.BindView;

public class SendFragment extends BaseFragment implements SendFragmentView {

    public static final int LAYOUT = R.layout.fragment_send;
    public static final String TAG = "SendFragment";
    public static final String ADDRESS = "address_qr_code";
    public static final String AMOUNT = "amount_qr_code";

    private boolean mIsQrCode = false;

    SendFragmentPresenterImpl mSendFragmentPresenter;

    @BindView(R.id.et_receivers_address)
    TextInputEditText mTextInputEditTextAddress;
    @BindView(R.id.et_amount)
    TextInputEditText mTextInputEditTextAmount;
    @BindView(R.id.et_pin)
    TextInputEditText mTextInputEditTextPin;

    public static SendFragment newInstance(String address, double amount) {
        SendFragment sendFragment = new SendFragment();
        Bundle args = new Bundle();
        args.putString(ADDRESS, address);
        args.putDouble(AMOUNT, amount);
        sendFragment.setArguments(args);
        return sendFragment;
    }

    public static SendFragment newInstance() {
        SendFragment sendFragment = new SendFragment();
        return sendFragment;
    }

    @Override
    protected void createPresenter() {
        mSendFragmentPresenter = new SendFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mSendFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        if(getArguments() != null) {
            mTextInputEditTextAddress.setText(getArguments().getString(ADDRESS));
            mTextInputEditTextAmount.setText(String.valueOf(getArguments().getDouble(AMOUNT)));
        }
        mTextInputEditTextPin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i== KeyEvent.KEYCODE_ENTER){
                    hideKeyBoard();
                }
                return false;
            }
        });
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
