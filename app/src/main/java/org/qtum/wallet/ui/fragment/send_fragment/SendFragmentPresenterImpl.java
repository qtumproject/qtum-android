package org.qtum.wallet.ui.fragment.send_fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.bitcoinj.script.Script;
import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.datastorage.QtumNetworkState;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.CurrencyToken;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.CallSmartContractRequest;
import org.qtum.wallet.model.gson.FeePerKb;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.model.gson.token_balance.Balance;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.currency_fragment.CurrencyFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinDialogFragment;
import org.qtum.wallet.ui.fragment.qr_code_recognition_fragment.QrCodeRecognitionFragment;
import org.qtum.wallet.utils.ContractBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SendFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SendFragmentPresenter {

    private SendFragmentView mSendFragmentView;
    private SendFragmentInteractorImpl mSendBaseFragmentInteractor;
    private UpdateService mUpdateService;
    private Context mContext;
    private NetworkStateReceiver mNetworkStateReceiver;
    private boolean mNetworkConnectedFlag = false;
    private List<Token> mTokenList;
    private double minFee;
    private double maxFee = 0.2;

    private int minGasPrice;
    private int maxGasPrice = 120;

    private int minGasLimit = 100000;
    private int maxGasLimit = 5000000;

    private static final int REQUEST_CAMERA = 3;
    private boolean OPEN_QR_CODE_FRAGMENT_FLAG = false;


    SendFragmentPresenterImpl(SendFragmentView sendFragmentView) {
        mSendFragmentView = sendFragmentView;
        mContext = getView().getContext();
        mSendBaseFragmentInteractor = new SendFragmentInteractorImpl(mContext);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().getMainActivity().setIconChecked(3);
        getView().getMainActivity().subscribeServiceConnectionChangeEvent(new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if (isConnecting) {
                    mUpdateService = getView().getMainActivity().getUpdateService();
                    mUpdateService.removeBalanceChangeListener();

                    mUpdateService.addBalanceChangeListener(new BalanceChangeListener() {
                        @Override
                        public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
                            getView().getMainActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String balanceString = balance.toString();
                                    if (balanceString != null) {
                                        String unconfirmedBalanceString = unconfirmedBalance.toString();
                                        if (!TextUtils.isEmpty(unconfirmedBalanceString) && !unconfirmedBalanceString.equals("0")) {
                                            getView().updateBalance(String.format("%S QTUM", balanceString), String.format("%S QTUM", String.valueOf(unconfirmedBalance.floatValue())));
                                        } else {
                                            getView().updateBalance(String.format("%S QTUM", balanceString), null);
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        mNetworkStateReceiver = getView().getMainActivity().getNetworkReceiver();
        mNetworkStateReceiver.addNetworkStateListener(new NetworkStateListener() {

            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                mNetworkConnectedFlag = networkConnectedFlag;
            }
        });

        getView().getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == REQUEST_CAMERA) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        OPEN_QR_CODE_FRAGMENT_FLAG = true;
                    }
                }
            }
        });
    }

    public void searchAndSetUpCurrency(String currency) {
        for (Token token : getInteractor().getTokenList()) {
            if (token.getContractAddress().equals(currency)) {
                getView().setUpCurrencyField(new CurrencyToken(token.getContractName(), token));
                return;
            }
        }
    }

    public void onCurrencyChoose(Currency currency) {
        getView().setUpCurrencyField(currency);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().getMainActivity().removePermissionResultListener();
        mUpdateService.removeBalanceChangeListener();
        mNetworkStateReceiver.removeNetworkStateListener();
        //TODO:unsubscribe rx
    }

    @Override
    public SendFragmentView getView() {
        return mSendFragmentView;
    }

    @Override
    public void onClickQrCode() {

        if (getView().getMainActivity().checkPermission(Manifest.permission.CAMERA)) {
            openQrCodeFragment();
        } else {
            getView().getMainActivity().loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);
        }
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        if (OPEN_QR_CODE_FRAGMENT_FLAG) {
            openQrCodeFragment();
        }
    }

    private void openQrCodeFragment() {
        OPEN_QR_CODE_FRAGMENT_FLAG = false;
        QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
        getView().hideKeyBoard();
        getView().openInnerFragmentForResult(qrCodeRecognitionFragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTokenList = new ArrayList<>();
        for (Token token : getInteractor().getTokenList()) {
            if (token.isSubscribe()) {
                mTokenList.add(token);
            }
        }
        if (!mTokenList.isEmpty()) {
            getView().setUpCurrencyField(new Currency("Qtum " + getView().getContext().getString(R.string.default_currency)));
        } else {
            getView().hideCurrencyField();
        }

        minFee = QtumNetworkState.newInstance().getFeePerKb().getFeePerKb().doubleValue();
        getView().updateFee(minFee,maxFee);
        minGasPrice = QtumNetworkState.newInstance().getDGPInfo().getMingasprice();
        getView().updateGasPrice(minGasPrice, maxGasPrice);
        getView().updateGasLimit(minGasLimit, maxGasLimit);
    }

    private SendFragmentInteractorImpl getInteractor() {
        return mSendBaseFragmentInteractor;
    }

    @Override
    public void isQrCodeRecognition(boolean isQrCodeRecognition) {

        if (isQrCodeRecognition) {
            QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
            getView().openInnerFragmentForResult(qrCodeRecognitionFragment);

        }
    }

    @Override
    public void onResponse(String publicAddress, double amount, String tokenAddress) {
        String tokenName = validateTokenExistance(tokenAddress);
        getView().updateData(publicAddress, amount, tokenName);
    }

    private String validateTokenExistance(String tokenAddress) {

        if (TextUtils.isEmpty(tokenAddress)) {
            return "";
        }

        TinyDB tdb = new TinyDB(mContext);
        List<Token> tokenList = tdb.getTokenList();
        for (Token token : tokenList) {
            if (tokenAddress.equals(token.getContractAddress())) {
                return token.getContractName();
            }
        }
        getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.token_not_found), "Ok", BaseFragment.PopUpType.error);
        return "";
    }

    @Override
    public void onResponseError() {
        getView().errorRecognition();
    }

    @Override
    public void onCurrencyClick() {
        BaseFragment currencyFragment = CurrencyFragment.newInstance(getView().getContext());
        getView().openFragmentForResult(currencyFragment);
    }

    String availableAddress = null;

    @Override
    public void send(final String from, final String address, final String amount, final Currency currency, String feeString, final int gasLimit, final int gasPrice) {
        if (mNetworkConnectedFlag) {
            final double feeDouble = Double.valueOf(feeString);
            if (feeDouble < minFee || feeDouble > maxFee) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.error), mContext.getResources().getString(R.string.invalid_fee), "Ok", BaseFragment.PopUpType.error);
                return;
            }
            final String fee = validateFee(feeDouble);
            if ((TextUtils.isEmpty(amount)) || Float.valueOf(amount) <= 0) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.error), mContext.getResources().getString(org.qtum.wallet.R.string.transaction_amount_cant_be_zero), "Ok", BaseFragment.PopUpType.error);
                return;
            }

            PinDialogFragment pinDialogFragment = new PinDialogFragment();
            pinDialogFragment.setTouchIdFlag(getView().getMainActivity().checkTouchIdEnable());
            pinDialogFragment.addPinCallBack(new PinDialogFragment.PinCallBack() {
                @Override
                public void onSuccess() {
                    getView().setProgressDialog();
                    if (currency.getName().equals("Qtum " + mContext.getString(org.qtum.wallet.R.string.default_currency))) {
                        getInteractor().sendTx(from, address, amount, fee, new SendFragmentInteractorImpl.SendTxCallBack() {
                            @Override
                            public void onSuccess() {
                                getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.payment_completed_successfully), "Ok", BaseFragment.PopUpType.confirm);
                            }

                            @Override
                            public void onError(String error) {
                                getView().dismissProgressDialog();
                                getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.error), error, "Ok", BaseFragment.PopUpType.error);
                            }
                        });
                    } else {
                        for (final Token token : mTokenList) {
                            if (token.getContractAddress().equals(((CurrencyToken) currency).getToken().getContractAddress())) {

                                String resultAmount = amount;

                                if (token.getDecimalUnits() != null) {
                                    resultAmount = String.valueOf((int) (Double.valueOf(amount) * Math.pow(10, token.getDecimalUnits())));
                                    resultAmount = String.valueOf(Integer.valueOf(resultAmount));
                                }

                                TokenBalance tokenBalance = getView().getSocketService().getTokenBalance(token.getContractAddress());

                                availableAddress = null;

                                if (!from.equals("")) {
                                    for (Balance balance : tokenBalance.getBalances()) {
                                        if (balance.getAddress().equals(from)) {
                                            if (balance.getBalance().floatValue() >= Float.valueOf(resultAmount)) {
                                                availableAddress = balance.getAddress();
                                                break;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    for (Balance balance : tokenBalance.getBalances()) {
                                        if (balance.getBalance().floatValue() >= Float.valueOf(resultAmount)) {
                                            availableAddress = balance.getAddress();
                                            break;
                                        }
                                    }
                                }

                                if (TextUtils.isEmpty(availableAddress)) {
                                    getView().dismissProgressDialog();
                                    getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.error), mContext.getString(org.qtum.wallet.R.string.you_have_insufficient_funds_for_this_transaction), "Ok", BaseFragment.PopUpType.error);
                                    return;
                                }

                                ContractBuilder contractBuilder = new ContractBuilder();
                                List<ContractMethodParameter> contractMethodParameterList = new ArrayList<>();
                                ContractMethodParameter contractMethodParameterAddress = new ContractMethodParameter("_to", "address", address);

                                ContractMethodParameter contractMethodParameterAmount = new ContractMethodParameter("_value", "uint256", resultAmount);
                                contractMethodParameterList.add(contractMethodParameterAddress);
                                contractMethodParameterList.add(contractMethodParameterAmount);
                                contractBuilder.createAbiMethodParams("transfer", contractMethodParameterList).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<String>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                getView().dismissProgressDialog();
                                                getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.error), e.getMessage(), "Ok", BaseFragment.PopUpType.error);

                                            }

                                            @Override
                                            public void onNext(final String s) {
                                                String hash = s.substring(0, s.length() - 64);
                                                hash = hash.concat("0000000000000000000000000000000000000000000000000000000000000000");
                                                QtumService.newInstance().callSmartContract(token.getContractAddress(), new CallSmartContractRequest(new String[]{hash}))
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(new Subscriber<CallSmartContractResponse>() {
                                                            @Override
                                                            public void onCompleted() {

                                                            }

                                                            @Override
                                                            public void onError(Throwable e) {

                                                            }

                                                            @Override
                                                            public void onNext(CallSmartContractResponse callSmartContractResponse) {
                                                                if (!callSmartContractResponse.getItems().get(0).getExcepted().equals("None")) {
                                                                    getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.error), callSmartContractResponse.getItems().get(0).getExcepted(), "Ok", BaseFragment.PopUpType.error);
                                                                    return;
                                                                }
                                                                if(gasLimit<callSmartContractResponse.getItems().get(0).getGasUsed()){
                                                                    getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.error), callSmartContractResponse.getItems().get(0).getExcepted(), "Ok", BaseFragment.PopUpType.error);
                                                                    return;
                                                                }
                                                                createTx(s, token.getContractAddress(), availableAddress, gasLimit, gasPrice, fee);
                                                            }
                                                        });
                                            }
                                        });

                                break;
                            }
                        }
                    }
                }

                @Override
                public void onError() {

                }
            });
            pinDialogFragment.show(getView().getFragmentManager(), pinDialogFragment.getClass().getCanonicalName());

        } else {
            getView().setAlertDialog(mContext.getString(org.qtum.wallet.R.string.no_internet_connection), mContext.getString(org.qtum.wallet.R.string.please_check_your_network_settings), "Ok", BaseFragment.PopUpType.error);
        }
    }

    private String validateFee(Double fee) {
        String pattern = "##0.00000000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(fee);
    }

    private void createTx(final String abiParams, final String contractAddress, String senderAddress, final int gasLimit, final int gasPrice, final String fee) {
        getInteractor().getUnspentOutputs(senderAddress, new SendFragmentInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {

                ContractBuilder contractBuilder = new ContractBuilder();
                Script script = contractBuilder.createMethodScript(abiParams, gasLimit, gasPrice,contractAddress);
                getInteractor().sendTx(contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice,getInteractor().getFeePerKb().getFeePerKb(), fee, mContext), new SendFragmentInteractorImpl.SendTxCallBack() {
                    @Override
                    public void onSuccess() {
                        getView().setAlertDialog(getView().getContext().getString(org.qtum.wallet.R.string.payment_completed_successfully), "Ok", BaseFragment.PopUpType.confirm);
                    }

                    @Override
                    public void onError(String error) {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog(getView().getContext().getString(org.qtum.wallet.R.string.error), error, "Ok", BaseFragment.PopUpType.error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(getView().getContext().getString(org.qtum.wallet.R.string.error), error, "Ok", BaseFragment.PopUpType.error);
            }
        });

    }

    private void calculateChangeInBalance(History history, List<String> addresses) {
        BigDecimal changeInBalance = calculateVout(history, addresses).subtract(calculateVin(history, addresses));
        history.setChangeInBalance(changeInBalance);
    }

    private BigDecimal calculateVin(History history, List<String> addresses) {
        BigDecimal totalVin = new BigDecimal("0.0");
        boolean equals = false;
        for (Vin vin : history.getVin()) {
            for (String address : addresses) {
                if (vin.getAddress().equals(address)) {
                    vin.setOwnAddress(true);
                    equals = true;
                }
            }
        }
        if (equals) {
            totalVin = history.getAmount();
        }
        return totalVin;
    }

    private BigDecimal calculateVout(History history, List<String> addresses) {
        BigDecimal totalVout = new BigDecimal("0.0");
        for (Vout vout : history.getVout()) {
            for (String address : addresses) {
                if (vout.getAddress().equals(address)) {
                    vout.setOwnAddress(true);
                    totalVout = totalVout.add(vout.getValue());
                }
            }
        }
        return totalVout;
    }

}