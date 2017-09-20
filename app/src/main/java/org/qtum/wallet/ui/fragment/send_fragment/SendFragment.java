package org.qtum.wallet.ui.fragment.send_fragment;

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
import android.widget.SeekBar;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class SendFragment extends BaseFragment implements SendFragmentView {

    private static final String IS_QR_CODE_RECOGNITION = "is_qr_code_recognition";
    private static final String ADDRESS = "address";
    private static final String TOKEN = "tokenAddr";
    private static final String AMOUNT = "amount";
    private final double INITIAL_FEE = 0.1;

    @BindView(org.qtum.wallet.R.id.et_receivers_address)
    protected TextInputEditText mTextInputEditTextAddress;
    @BindView(org.qtum.wallet.R.id.et_amount)
    protected TextInputEditText mTextInputEditTextAmount;
    @BindView(org.qtum.wallet.R.id.til_receivers_address)
    protected TextInputLayout tilAdress;
    @BindView(org.qtum.wallet.R.id.til_amount)
    protected TextInputLayout tilAmount;
    @BindView(R.id.et_fee)
    protected TextInputEditText mTextInputEditTextFee;
    @BindView(R.id.til_fee)
    protected TextInputLayout tilFee;
    @BindView(R.id.ll_fee)
    LinearLayout mLinearLayoutFee;
    @BindView(org.qtum.wallet.R.id.bt_send) Button mButtonSend;
    @BindView(org.qtum.wallet.R.id.ibt_back) ImageButton mImageButtonBack;
    @BindView(org.qtum.wallet.R.id.tv_toolbar_send) TextView mTextViewToolBar;
    @BindView(org.qtum.wallet.R.id.rl_send) RelativeLayout mRelativeLayoutBase;
    @BindView(org.qtum.wallet.R.id.ll_currency)
    protected LinearLayout mLinearLayoutCurrency;
    @BindView(org.qtum.wallet.R.id.tv_currency)
    protected TextView mTextViewCurrency;
    @BindView(R.id.tv_max_fee)
    FontTextView mFontTextViewMaxFee;
    @BindView(R.id.tv_min_fee)
    FontTextView mFontTextViewMinFee;
    @BindView(org.qtum.wallet.R.id.bt_qr_code) ImageButton mButtonQrCode;
    @BindView(org.qtum.wallet.R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;
    @BindView(org.qtum.wallet.R.id.not_confirmed_balance_view) View notConfirmedBalancePlaceholder;
    @BindView(org.qtum.wallet.R.id.tv_placeholder_balance_value) TextView placeHolderBalance;
    @BindView(org.qtum.wallet.R.id.tv_placeholder_not_confirmed_balance_value) TextView placeHolderBalanceNotConfirmed;

    int mMinFee;
    int mMaxFee;
    int step = 100;

    protected SendFragmentPresenterImpl sendBaseFragmentPresenter;

    @OnClick({org.qtum.wallet.R.id.bt_qr_code, org.qtum.wallet.R.id.ibt_back, org.qtum.wallet.R.id.ll_currency})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.bt_qr_code:
                getPresenter().onClickQrCode();
                break;
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case org.qtum.wallet.R.id.ll_currency:
                getPresenter().onCurrencyClick();
                break;
        }
    }

    @OnClick(R.id.bt_send)
    public void onSendClick(){
        String[] sendInfo = new String[4];
        sendInfo[0] = mTextInputEditTextAddress.getText().toString();
        sendInfo[1] = mTextInputEditTextAmount.getText().toString();
        if(mLinearLayoutCurrency.getVisibility()==View.VISIBLE){
            sendInfo[2] = mTextViewCurrency.getText().toString();
        } else {
            sendInfo[2] = "Qtum "+getString(org.qtum.wallet.R.string.default_currency);
        }
        sendInfo[3] = mTextInputEditTextFee.getText().toString();
        getPresenter().send(sendInfo);
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
                .setCustomAnimations(org.qtum.wallet.R.anim.enter_from_right, org.qtum.wallet.R.anim.exit_to_left, org.qtum.wallet.R.anim.enter_from_left, org.qtum.wallet.R.anim.exit_to_right)
                .add(org.qtum.wallet.R.id.fragment_container_send_base, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void qrCodeRecognitionToolBar() {
        mButtonQrCode.setVisibility(View.GONE);
        mTextViewToolBar.setText(org.qtum.wallet.R.string.qr_code);
        mImageButtonBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendToolBar() {
        if (mButtonQrCode != null) {
            mTextViewToolBar.setText(org.qtum.wallet.R.string.send);
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



        mTextInputEditTextFee.setText(String.valueOf(INITIAL_FEE));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = (mMinFee + (progress * step))/100000000.;
                mTextInputEditTextFee.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        setAlertDialog(getString(org.qtum.wallet.R.string.error_qrcode_recognition_try_again),"Ok",PopUpType.error);
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

    @Override
    public void updateFee(double minFee, double maxFee) {
        mFontTextViewMaxFee.setText(String.valueOf(maxFee));
        mFontTextViewMinFee.setText(String.valueOf(minFee));
        mMinFee =Double.valueOf( minFee*100000000).intValue();
        mMaxFee = Double.valueOf(maxFee*100000000).intValue();
        mSeekBar.setMax( (mMaxFee - mMinFee) / step );
        //mSeekBar.setProgress((int)(INITIAL_FEE*100000000));
    }

    public void onResponse(String pubAddress, Double amount, String tokenAddress) {
        getPresenter().onResponse(pubAddress, amount, tokenAddress);
    }

    public void onCurrencyChoose(String currency){
        getPresenter().onCurrencyChoose(currency);
    }

    public void onResponseError() {
        setAlertDialog(getString(org.qtum.wallet.R.string.invalid_qr_code),"OK", PopUpType.error);
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