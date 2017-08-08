package com.pixelplex.qtum.ui.fragment.SendFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import butterknife.BindView;
import butterknife.OnClick;


public abstract class SendFragment extends BaseFragment implements SendFragmentView {

    private static final String IS_QR_CODE_RECOGNITION = "is_qr_code_recognition";
    private static final String ADDRESS = "address";
    private static final String TOKEN = "tokenAddr";
    private static final String AMOUNT = "amount";

    @BindView(R.id.et_receivers_address)
    protected TextInputEditText mTextInputEditTextAddress;
    @BindView(R.id.et_amount)
    protected TextInputEditText mTextInputEditTextAmount;
    @BindView(R.id.til_receivers_address)
    protected TextInputLayout tilAdress;
    @BindView(R.id.til_amount)
    protected TextInputLayout tilAmount;
    @BindView(R.id.bt_send) Button mButtonSend;
    @BindView(R.id.ibt_back) ImageButton mImageButtonBack;
    @BindView(R.id.tv_toolbar_send) TextView mTextViewToolBar;
    @BindView(R.id.rl_send) RelativeLayout mRelativeLayoutBase;
    @BindView(R.id.ll_currency)
    protected LinearLayout mLinearLayoutCurrency;
    @BindView(R.id.tv_currency)
    protected TextView mTextViewCurrency;
    @BindView(R.id.bt_qr_code) ImageButton mButtonQrCode;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.not_confirmed_balance_view) View notConfirmedBalancePlaceholder;
    @BindView(R.id.tv_placeholder_balance_value) TextView placeHolderBalance;
    @BindView(R.id.tv_placeholder_not_confirmed_balance_value) TextView placeHolderBalanceNotConfirmed;


    protected SendFragmentPresenterImpl sendBaseFragmentPresenter;

    @OnClick({R.id.bt_qr_code,R.id.ibt_back, R.id.ll_currency})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_qr_code:
                getPresenter().onClickQrCode();
                break;
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.ll_currency:
                getPresenter().onCurrencyClick();
                break;
        }
    }

    public static BaseFragment newInstance(boolean qrCodeRecognition, String address, String amount, String tokenAddress, Context context) {
        BaseFragment sendFragment = Factory.instantiateFragment(context, SendFragment.class);
        Bundle args = new Bundle();
        args.putBoolean(IS_QR_CODE_RECOGNITION, qrCodeRecognition);
        args.putString(ADDRESS,address);
        args.putString(TOKEN,tokenAddress);
        args.putString(AMOUNT,amount);
        sendFragment.setArguments(args);
        return sendFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().isQrCodeRecognition(getArguments().getBoolean(IS_QR_CODE_RECOGNITION));
        getArguments().putBoolean(IS_QR_CODE_RECOGNITION, false);
    }

    @Override
    protected void createPresenter() {
        sendBaseFragmentPresenter = new SendFragmentPresenterImpl(this);
    }

    @Override
    protected SendFragmentPresenterImpl getPresenter() {
        return sendBaseFragmentPresenter;
    }

    @Override
    public void openInnerFragmentForResult(Fragment fragment) {
        int code_response = 200;
        fragment.setTargetFragment(this, code_response);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.fragment_container_send_base, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void qrCodeRecognitionToolBar() {
        mButtonQrCode.setVisibility(View.GONE);
        mTextViewToolBar.setText(R.string.qr_code);
        mImageButtonBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendToolBar() {
        if (mButtonQrCode != null) {
            mTextViewToolBar.setText(R.string.send);
            mButtonQrCode.setVisibility(View.VISIBLE);
            mImageButtonBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        showBottomNavView(true);
        ((MainActivity) getActivity()).setIconChecked(3);
        mImageButtonBack.setVisibility(View.GONE);
        mRelativeLayoutBase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    hideKeyBoard();
                }
            }
        });
        String address = getArguments().getString(ADDRESS, "");
        String amount = getArguments().getString(AMOUNT, "");
        mTextInputEditTextAmount.setText(amount);
        mTextInputEditTextAddress.setText(address);
    }

    @Override
    public void updateData(String publicAddress, double amount, String tokenAddress) {
        mTextInputEditTextAddress.setText(publicAddress);
        mTextInputEditTextAmount.setText(String.valueOf(amount));
        if(!TextUtils.isEmpty(tokenAddress)) {
            mLinearLayoutCurrency.setVisibility(View.VISIBLE);
            mTextViewCurrency.setText(tokenAddress);
        }
    }

    @Override
    public void errorRecognition() {
        setAlertDialog("Error QR-code Recognition. Try Again","Ok",PopUpType.error);
    }

    @Override
    public void setUpCurrencyField(String currency) {
        mLinearLayoutCurrency.setVisibility(View.VISIBLE);
        mTextViewCurrency.setText(currency);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void hideCurrencyField() {
        mLinearLayoutCurrency.setVisibility(View.GONE);
    }

    @Override
    public UpdateService getSocketService() {
        return getMainActivity().getUpdateService();
    }

    @Override
    public void setAdressAndAmount(final String address, final String anount) {
        mTextInputEditTextAddress.post(new Runnable() {
            @Override
            public void run() {
                mTextInputEditTextAddress.setText(address);
                mTextInputEditTextAmount.setText(anount);
            }
        });
    }


    public void onResponse(String pubAddress, Double amount, String tokenAddress) {
        getPresenter().onResponse(pubAddress, amount, tokenAddress);
    }

    public void onCurrencyChoose(String currency){
        getPresenter().onCurrencyChoose(currency);
    }

    public void onResponseError() {
        setAlertDialog("Invalid QR Code","OK", PopUpType.error);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
        placeHolderBalance.setText(balance);
        if(!TextUtils.isEmpty(unconfirmedBalance)) {
            notConfirmedBalancePlaceholder.setVisibility(View.VISIBLE);
            placeHolderBalanceNotConfirmed.setText(unconfirmedBalance);
        } else {
            notConfirmedBalancePlaceholder.setVisibility(View.GONE);
        }
    }
}