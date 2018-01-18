package org.qtum.wallet.ui.fragment.send_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.CurrencyToken;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.model.gson.token_balance.Balance;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SendPresenterImpl extends BaseFragmentPresenterImpl implements SendPresenter {

    private SendView mSendFragmentView;
    private SendInteractor mSendBaseFragmentInteractor;
    private boolean mNetworkConnectedFlag = false;
    private List<Token> mTokenList;
    private double minFee;
    private double maxFee = 1;

    private int minGasPrice;
    private int maxGasPrice = 120;

    private int minGasLimit = 100000;
    private int maxGasLimit = 5000000;

    public SendPresenterImpl(SendView sendFragmentView, SendInteractor interactor) {
        mSendFragmentView = sendFragmentView;
        mSendBaseFragmentInteractor = interactor;
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
            getView().setUpCurrencyField(R.string.default_currency);
        } else {
            getView().hideCurrencyField();
        }
        minFee = getInteractor().getFeePerKb().doubleValue();
        getView().updateFee(minFee, maxFee);
        minGasPrice = getInteractor().getMinGasPrice();
        getView().updateGasPrice(minGasPrice, maxGasPrice);
        getView().updateGasLimit(minGasLimit, maxGasLimit);

    }

    @Override
    public void handleBalanceChanges(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                String balanceString = balance.toString();
                if (balanceString != null) {
                    return Observable.just(balanceString);
                } else {
                    return Observable.error(new Throwable("Balance is null"));
                }
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String balanceString) {
                        getView().handleBalanceUpdating(balanceString, unconfirmedBalance);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    @Override
    public void searchAndSetUpCurrency(String currency) {
        for (Token token : getInteractor().getTokenList()) {
            if (token.getContractAddress().equals(currency)) {
                getView().setUpCurrencyField(new CurrencyToken(token.getContractName(), token));
                return;
            }
        }
        getView().setAlertDialog(R.string.token_not_found, "Ok", BaseFragment.PopUpType.error);
    }

    @Override
    public void onCurrencyChoose(Currency currency) {
        getView().setUpCurrencyField(currency);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().removePermissionResultListener();
    }

    @Override
    public SendView getView() {
        return mSendFragmentView;
    }

    private SendInteractor getInteractor() {
        return mSendBaseFragmentInteractor;
    }

    @Override
    public void onResponse(String publicAddress, double amount, String tokenAddress) {
        getView().updateData(publicAddress, amount);
        if (!tokenAddress.isEmpty()) {
            searchAndSetUpCurrency(tokenAddress);
        }
    }

    @Override
    public void onResponseError() {
        getView().errorRecognition();
    }

    private String availableAddress = null;
    public String params = null;

    @Override
    public void send() {
        if (mNetworkConnectedFlag) {
            final double feeDouble = Double.valueOf(getView().getFeeInput().replace(',', '.'));
            if (feeDouble < minFee || feeDouble > maxFee) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(org.qtum.wallet.R.string.error, R.string.invalid_fee, "Ok", BaseFragment.PopUpType.error);
                return;
            }

            if (!getView().isValidAmount()) {
                return;
            }
            getView().hideKeyBoard();
            getView().showPinDialog();

        } else {
            getView().setAlertDialog(org.qtum.wallet.R.string.no_internet_connection, org.qtum.wallet.R.string.please_check_your_network_settings, "Ok", BaseFragment.PopUpType.error);
        }
    }

    public boolean isAmountValid(String amount) {
        BigDecimal bigAmount = new BigDecimal(amount);
        BigDecimal pattern = new BigDecimal("2").pow(256);
        return bigAmount.compareTo(pattern) <= 0;
    }

    @Override
    public void onPinSuccess() {
        Currency currency = getView().getCurrency();
        String from = getView().getFromAddress();
        String address = getView().getAddressInput();
        String amount = getView().getAmountInput();
        final double feeDouble = Double.valueOf(getView().getFeeInput());
        final String fee = validateFee(feeDouble);
        int gasLimit = getView().getGasLimitInput();
        int gasPrice = getView().getGasPriceInput();
        if (currency.getName().equals("Qtum " + getView().getStringValue(org.qtum.wallet.R.string.default_currency))) {
            if (isAmountValid(amount)) {
                getInteractor().sendTx(from, address, amount, fee, getView().getSendTransactionCallback());
            } else {
                getView().setAlertDialog(getView().getContext().getString(R.string.amount_too_big), getView().getContext().getString(R.string.ok), BaseFragment.PopUpType.error);
            }
        } else {
            for (final Token token : mTokenList) {
                String contractAddress = token.getContractAddress();
                if (contractAddress.equals(((CurrencyToken) currency).getToken().getContractAddress())) {
                    String resultAmount = amount;
                    if (token.getDecimalUnits() != null) {

                        BigDecimal multiplyPow = new BigDecimal("10").pow(token.getDecimalUnits());

                        if(!validateDecimalUnits(resultAmount, token.getDecimalUnits())){
                            getView().dismissProgressDialog();
                            getView().setAlertDialog(org.qtum.wallet.R.string.error, getView().getContext().getString(R.string.amount_too_big), "Ok", BaseFragment.PopUpType.error);
                            return;
                        }

                        resultAmount = new BigDecimal(amount).multiply(multiplyPow).toBigInteger().toString();
                    }

                    if (!isAmountValid(resultAmount)) {
                        getView().setAlertDialog(getView().getContext().getString(R.string.amount_too_big), getView().getContext().getString(R.string.ok), BaseFragment.PopUpType.error);
                    }
                    TokenBalance tokenBalance = getView().getTokenBalance(contractAddress);
                    availableAddress = null;
                    if (!from.equals("")) {
                        for (Balance balance : tokenBalance.getBalances()) {
                            if (balance.getAddress().equals(from)) {
                                if (balance.getBalance().compareTo(new BigDecimal(resultAmount)) >= 0) {
                                    availableAddress = balance.getAddress();
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    } else {
                        for (Balance balance : tokenBalance.getBalances()) {
                            if (balance.getBalance().compareTo(new BigDecimal(resultAmount)) >= 0) {
                                availableAddress = balance.getAddress();
                                break;
                            }
                        }
                    }
                    if (!getView().isValidAvailableAddress(availableAddress)) {
                        return;
                    }
                    createAbiMethodParams(address, resultAmount, token, fee, gasPrice, gasLimit);
                    break;
                }
            }
        }
    }

    boolean validateDecimalUnits(String amount, int decimalUnits) {
        int commaPosition = amount.indexOf(".");
        return commaPosition < 0 || amount.substring(commaPosition + 1).length() <= decimalUnits;
    }

    private void createAbiMethodParams(String address, String resultAmount, final Token token, final String fee, final int gasPrice, final int gasLimit) {
        getInteractor().createAbiMethodParamsObservable(address, resultAmount, "transfer")
                .flatMap(new Func1<String, Observable<CallSmartContractResponse>>() {
                    @Override
                    public Observable<CallSmartContractResponse> call(String s) {
                        params = s;
                        return getInteractor().callSmartContractObservable(token, s, availableAddress);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CallSmartContractResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().dismissProgressDialog();
                        getView().setAlertDialog(org.qtum.wallet.R.string.error, e.getMessage(), "Ok", BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(CallSmartContractResponse response) {
                        if (!response.getItems().get(0).getExcepted().equals("None")) {
                            getView().setAlertDialog(org.qtum.wallet.R.string.error,
                                    response.getItems().get(0).getExcepted(), "Ok", BaseFragment.PopUpType.error);
                            return;
                        }
                        createTx(params, token.getContractAddress(), availableAddress, gasLimit, gasPrice, fee);
                    }
                });

    }

    private String validateFee(Double fee) {
        return getInteractor().getValidatedFee(fee);
    }

    private void createTx(final String abiParams, final String contractAddress, String senderAddress, final int gasLimit, final int gasPrice, final String fee) {
        getInteractor().getUnspentOutputs(senderAddress, new SendInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                String txHex = getInteractor().createTransactionHash(abiParams, contractAddress, unspentOutputs, gasLimit, gasPrice, fee);
                getInteractor().sendTx(txHex, getView().getSendTransactionCallback());
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
            }
        });
    }

    @Override
    public void updateNetworkSate(boolean networkConnectedFlag) {
        mNetworkConnectedFlag = networkConnectedFlag;
    }

    public double getMinFee() {
        return minFee;
    }

    public List<Token> getTokenList() {
        return mTokenList;
    }

    public String getAvailableAddress() {
        return availableAddress;
    }

    public void setTokenList(List<Token> tokenList) {
        this.mTokenList = tokenList;
    }

    public void setMinFee(double minFee) {
        this.minFee = minFee;
    }

    public void setMaxFee(double maxFee) {
        this.maxFee = maxFee;
    }
}