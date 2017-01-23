package org.qtum.mromanovsky.qtum.ui.fragment.SendFragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.bt_qr_code)
    ImageButton mButtonQrCode;
    @BindView(R.id.bt_send)
    Button mButtonSend;

    @OnClick({R.id.bt_qr_code,R.id.bt_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_qr_code:
                getPresenter().onClickQrCode();
                break;
            case R.id.bt_send:
                String[] sendInfo = new String[3];
                sendInfo[0] = mTextInputEditTextAddress.getText().toString();
                sendInfo[1] = mTextInputEditTextAmount.getText().toString();
                sendInfo[2] = mTextInputEditTextPin.getText().toString();
                getPresenter().send(sendInfo);
        }
    }

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
    protected SendFragmentPresenterImpl getPresenter() {
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
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
