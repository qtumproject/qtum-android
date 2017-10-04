package org.qtum.wallet.ui.fragment.send_fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.currency_fragment.CurrencyFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinDialogFragment;
import org.qtum.wallet.ui.fragment.qr_code_recognition_fragment.QrCodeRecognitionFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontTextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class SendFragment extends BaseFragment implements SendView {

    private static final int REQUEST_CAMERA = 3;
    private boolean OPEN_QR_CODE_FRAGMENT_FLAG = false;
    private static final String IS_QR_CODE_RECOGNITION = "is_qr_code_recognition";
    private static final String ADDRESS = "address";
    private static final String TOKEN = "tokenAddr";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";

    private static final String ADDRESS_FROM = "address_from";

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
    @BindView(org.qtum.wallet.R.id.bt_send)
    Button mButtonSend;
    @BindView(org.qtum.wallet.R.id.ibt_back)
    ImageButton mImageButtonBack;
    @BindView(org.qtum.wallet.R.id.tv_toolbar_send)
    TextView mTextViewToolBar;
    @BindView(org.qtum.wallet.R.id.rl_send)
    RelativeLayout mRelativeLayoutBase;
    @BindView(org.qtum.wallet.R.id.ll_currency)
    protected LinearLayout mLinearLayoutCurrency;
    @BindView(org.qtum.wallet.R.id.tv_currency)
    protected TextView mTextViewCurrency;
    @BindView(R.id.tv_max_fee)
    FontTextView mFontTextViewMaxFee;
    @BindView(R.id.tv_min_fee)
    FontTextView mFontTextViewMinFee;
    @BindView(org.qtum.wallet.R.id.bt_qr_code)
    ImageButton mButtonQrCode;
    @BindView(org.qtum.wallet.R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;
    @BindView(org.qtum.wallet.R.id.not_confirmed_balance_view)
    View notConfirmedBalancePlaceholder;
    @BindView(org.qtum.wallet.R.id.tv_placeholder_balance_value)
    TextView placeHolderBalance;
    @BindView(org.qtum.wallet.R.id.tv_placeholder_not_confirmed_balance_value)
    TextView placeHolderBalanceNotConfirmed;

    int mMinFee;
    int mMaxFee;
    int step = 100;
    Currency mCurrency;
    AlertDialogCallBack mAlertDialogCallBack;
    View.OnTouchListener mOnTouchListener;
    private NetworkStateReceiver mNetworkStateReceiver;
    private UpdateService mUpdateService;

    protected SendPresenter sendBaseFragmentPresenter;
    private boolean sendFrom = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMainActivity().setIconChecked(3);
        getMainActivity().subscribeServiceConnectionChangeEvent(new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if (isConnecting) {
                    mUpdateService = getUpdateService();
                    mUpdateService.removeBalanceChangeListener();

                    mUpdateService.addBalanceChangeListener(new BalanceChangeListener() {
                        @Override
                        public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
                            getPresenter().handleBalanceChanges(unconfirmedBalance, balance);
                        }
                    });
                }
            }
        });
        getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == REQUEST_CAMERA) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        OPEN_QR_CODE_FRAGMENT_FLAG = true;
                    }
                }
            }
        });

        mNetworkStateReceiver = getMainActivity().getNetworkReceiver();
        mNetworkStateReceiver.addNetworkStateListener(new NetworkStateListener() {
            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                getPresenter().updateNetworkSate(networkConnectedFlag);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isQrCodeRecognition(getArguments().getBoolean(IS_QR_CODE_RECOGNITION));
        getArguments().putBoolean(IS_QR_CODE_RECOGNITION, false);
        String currency = getArguments().getString(CURRENCY, "");
        if (!currency.equals("")) {
            getPresenter().searchAndSetUpCurrency(currency);
        }

        if (OPEN_QR_CODE_FRAGMENT_FLAG) {
            openQrCodeFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNetworkStateReceiver.removeNetworkStateListener();
        mUpdateService.removeBalanceChangeListener();
    }

    private void isQrCodeRecognition(boolean isQrCodeRecognition) {
        if (isQrCodeRecognition) {
            QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
            openInnerFragmentForResult(qrCodeRecognitionFragment);
        }
    }


    @OnClick({org.qtum.wallet.R.id.bt_qr_code, org.qtum.wallet.R.id.ibt_back, org.qtum.wallet.R.id.ll_currency})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.bt_qr_code:
                onClickQrCode();
                break;
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case org.qtum.wallet.R.id.ll_currency:
                onCurrencyClick();
                break;
        }
    }

    private void onClickQrCode() {
        if (getMainActivity().checkPermission(Manifest.permission.CAMERA)) {
            openQrCodeFragment();
        } else {
            getMainActivity().loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);
        }
    }

    private void onCurrencyClick() {
        BaseFragment currencyFragment = CurrencyFragment.newInstance(getView().getContext());
        openFragmentForResult(currencyFragment);
    }

    private void openQrCodeFragment() {
        OPEN_QR_CODE_FRAGMENT_FLAG = false;
        QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
        hideKeyBoard();
        openInnerFragmentForResult(qrCodeRecognitionFragment);
    }

    @OnClick(R.id.bt_send)
    public void onSendClick() {
        getPresenter().send();
    }

    @Override
    public String getAddressInput() {
        return mTextInputEditTextAddress.getText().toString();
    }

    @Override
    public String getAmountInput() {
        return mTextInputEditTextAmount.getText().toString();
    }

    @Override
    public String getFeeInput() {
        return mTextInputEditTextFee.getText().toString();
    }

    @Override
    public String getFromAddress() {
        return getArguments().getString(ADDRESS_FROM, "");
    }

    @Override
    public Currency getCurrency() {
        return mCurrency;
    }

    public static BaseFragment newInstance(boolean qrCodeRecognition, String address, String amount, String tokenAddress, Context context) {
        BaseFragment sendFragment = Factory.instantiateFragment(context, SendFragment.class);
        Bundle args = new Bundle();
        args.putBoolean(IS_QR_CODE_RECOGNITION, qrCodeRecognition);
        args.putString(ADDRESS, address);
        args.putString(TOKEN, tokenAddress);
        args.putString(AMOUNT, amount);
        sendFragment.setArguments(args);
        return sendFragment;
    }

    public static BaseFragment newInstance(String addressFrom, String addressTo, String amount, String currency, Context context) {
        BaseFragment sendFragment = Factory.instantiateFragment(context, SendFragment.class);
        Bundle args = new Bundle();
        args.putString(ADDRESS_FROM, addressFrom);
        args.putString(ADDRESS, addressTo);
        args.putString(AMOUNT, amount);
        args.putString(CURRENCY, currency);
        sendFragment.setArguments(args);
        return sendFragment;
    }

    @Override
    protected void createPresenter() {
        sendBaseFragmentPresenter = new SendPresenterImpl(this, new SendInteractorImpl(getContext()));
    }

    @Override
    protected SendPresenter getPresenter() {
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
        mAlertDialogCallBack = new AlertDialogCallBack() {
            @Override
            public void onButtonClick() {

            }

            @Override
            public void onButton2Click() {
                mTextInputEditTextAddress.setText("");
                mTextInputEditTextAmount.setText("");
                mCurrency = new Currency("Qtum " + getContext().getString(R.string.default_currency));
                mTextViewCurrency.setText(mCurrency.getName());
                sendFrom = false;
                getArguments().putString(ADDRESS_FROM, "");
            }
        };
        mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sendFrom) {
                    setAlertDialog("Attention", "By changing address or currency, transaction will be processed as a regular transfer", "Cancel", "Continue", PopUpType.confirm, mAlertDialogCallBack);
                    return true;
                }
                return false;
            }
        };
        mCurrency = new Currency("Qtum " + getContext().getString(R.string.default_currency));
        showBottomNavView(true);
        ((MainActivity) getActivity()).setIconChecked(3);
        mImageButtonBack.setVisibility(View.GONE);
        mRelativeLayoutBase.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    hideKeyBoard();
                }
            }
        });

        String address = getArguments().getString(ADDRESS, "");
        String amount = getArguments().getString(AMOUNT, "");
        if (!getArguments().getString(ADDRESS_FROM, "").equals("")) {
            sendFrom = true;
        }
        mTextInputEditTextAmount.setText(amount);
        mTextInputEditTextAddress.setText(address);

        mTextInputEditTextFee.setText(String.valueOf(INITIAL_FEE));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = (mMinFee + (progress * step)) / 100000000.;
                mTextInputEditTextFee.setText(new DecimalFormat("#.########").format(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mTextInputEditTextAddress.setOnTouchListener(mOnTouchListener);
        mLinearLayoutCurrency.setOnTouchListener(mOnTouchListener);

    }

    @Override
    public void updateData(String publicAddress, double amount, String tokenAddress) {
        mTextInputEditTextAddress.setText(publicAddress);
        mTextInputEditTextAmount.setText(String.valueOf(amount));
        if (!TextUtils.isEmpty(tokenAddress)) {
            mLinearLayoutCurrency.setVisibility(View.VISIBLE);
            mTextViewCurrency.setText(tokenAddress);
        }
    }

    @Override
    public void errorRecognition() {
        setAlertDialog(getString(org.qtum.wallet.R.string.error_qrcode_recognition_try_again), "Ok", PopUpType.error);
    }

    @Override
    public void setUpCurrencyField(Currency currency) {
        mLinearLayoutCurrency.setVisibility(View.VISIBLE);
        mTextViewCurrency.setText(currency.getName());
        mCurrency = currency;
    }

    @Override
    public void setUpCurrencyField(@StringRes int defaultCurrId) {
        Currency currency = new Currency("Qtum " + getContext().getString(defaultCurrId));
        setUpCurrencyField(currency);
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
        mFontTextViewMaxFee.setText(new DecimalFormat("#.########").format(maxFee));
        mFontTextViewMinFee.setText(new DecimalFormat("#.########").format(minFee));
        mMinFee = Double.valueOf(minFee * 100000000).intValue();
        mMaxFee = Double.valueOf(maxFee * 100000000).intValue();
        mSeekBar.setMax((mMaxFee - mMinFee) / step);
        //mSeekBar.setProgress((int)(INITIAL_FEE*100000000));
    }

    public void onResponse(String pubAddress, Double amount, String tokenAddress) {
        getPresenter().onResponse(pubAddress, amount, tokenAddress);
    }

    public void onCurrencyChoose(Currency currency) {
        getPresenter().onCurrencyChoose(currency);
    }

    public void onResponseError() {
        setAlertDialog(getString(org.qtum.wallet.R.string.invalid_qr_code), "OK", PopUpType.error);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
        placeHolderBalance.setText(balance);
        if (!TextUtils.isEmpty(unconfirmedBalance)) {
            notConfirmedBalancePlaceholder.setVisibility(View.VISIBLE);
            placeHolderBalanceNotConfirmed.setText(unconfirmedBalance);
        } else {
            notConfirmedBalancePlaceholder.setVisibility(View.GONE);
        }
    }

    private UpdateService getUpdateService() {
        return getMainActivity().getUpdateService();
    }

    @Override
    public void handleBalanceUpdating(String balanceString, BigDecimal unconfirmedBalance) {
        String unconfirmedBalanceString = unconfirmedBalance.toString();
        if (!TextUtils.isEmpty(unconfirmedBalanceString) && !unconfirmedBalanceString.equals("0")) {
            updateBalance(String.format("%S QTUM", balanceString), String.format("%S QTUM", String.valueOf(unconfirmedBalance.floatValue())));
        } else {
            updateBalance(String.format("%S QTUM", balanceString), null);
        }
    }

    @Override
    public String getStringValue(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public void removePermissionResultListener() {
        getMainActivity().removePermissionResultListener();
    }

    @Override
    public boolean isTokenEmpty(String tokenAddress) {
        return TextUtils.isEmpty(tokenAddress);
    }

    @Override
    public boolean isValidAmount() {
        String amount = getAmountInput();
        if ((TextUtils.isEmpty(amount)) || Float.valueOf(amount) <= 0) {
            dismissProgressDialog();
            setAlertDialog(org.qtum.wallet.R.string.error, org.qtum.wallet.R.string.transaction_amount_cant_be_zero, "Ok", BaseFragment.PopUpType.error);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showPinDialog() {
        PinDialogFragment pinDialogFragment = new PinDialogFragment();
        pinDialogFragment.setTouchIdFlag(getMainActivity().checkTouchIdEnable());
        pinDialogFragment.addPinCallBack(callback);
        pinDialogFragment.show(getFragmentManager(), pinDialogFragment.getClass().getCanonicalName());
    }

    PinDialogFragment.PinCallBack callback = new PinDialogFragment.PinCallBack() {
        @Override
        public void onSuccess() {
            setProgressDialog();
            getPresenter().onPinSuccess();
        }

        @Override
        public void onError() {

        }
    };


    private SendInteractorImpl.SendTxCallBack sendCallback = new SendInteractorImpl.SendTxCallBack() {
        @Override
        public void onSuccess() {
            setAlertDialog(org.qtum.wallet.R.string.payment_completed_successfully, "Ok", BaseFragment.PopUpType.confirm);
        }

        @Override
        public void onError(String error) {
            dismissProgressDialog();
            setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
        }
    };

    @Override
    public SendInteractorImpl.SendTxCallBack getSendTransactionCallback() {
        return sendCallback;
    }

    @Override
    public boolean isValidAvailableAddress(String availableAddress) {
        if (TextUtils.isEmpty(availableAddress)) {
            dismissProgressDialog();
            setAlertDialog(org.qtum.wallet.R.string.error,
                    org.qtum.wallet.R.string.you_have_insufficient_funds_for_this_transaction,
                    "Ok", BaseFragment.PopUpType.error);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public TokenBalance getTokenBalance(String contractAddress) {
        return getSocketService().getTokenBalance(contractAddress);
    }
}