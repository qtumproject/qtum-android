package org.qtum.wallet.ui.fragment.send_fragment;

import android.text.TextUtils;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.CurrencyToken;
import org.qtum.wallet.model.contract.Token;
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
import org.qtum.wallet.ui.fragment.pin_fragment.PinDialogFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SendPresenterImpl extends BaseFragmentPresenterImpl implements SendPresenter {

    private SendView mSendFragmentView;
    private SendInteractor mSendBaseFragmentInteractor;
    private UpdateService mUpdateService;
    private boolean mNetworkConnectedFlag = false;
    private List<Token> mTokenList;
    private double minFee;
    private double maxFee = 0.2;

    public SendPresenterImpl(SendView sendFragmentView, SendInteractor interactor) {
        mSendFragmentView = sendFragmentView;
        mSendBaseFragmentInteractor = interactor;
    }

    @Override
    public MainActivity.OnServiceConnectionChangeListener getServiceConnectionChangeListener() {
        return new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if (isConnecting) {
                    mUpdateService = getView().getUpdateService();
                    mUpdateService.removeBalanceChangeListener();

                    mUpdateService.addBalanceChangeListener(new BalanceChangeListener() {
                        @Override
                        public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
                            Observable.defer(new Func0<Observable<Boolean>>() {
                                @Override
                                public Observable<Boolean> call() {
                                    String balanceString = balance.toString();
                                    if (balanceString != null) {
                                        getView().handleBalanceUpdating(balanceString, unconfirmedBalance);
                                    }
                                    return Observable.just(true);
                                }
                            })
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Boolean>() {
                                        @Override
                                        public void call(Boolean aBoolean) {

                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    });
                        }
                    });
                }
            }
        };
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
        getView().removePermissionResultListener();
        mUpdateService.removeBalanceChangeListener();
        //TODO:unsubscribe rx
    }

    @Override
    public SendView getView() {
        return mSendFragmentView;
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
        getInteractor().getFeePerKbObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FeePerKb>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(FeePerKb feePerKb) {
                        getInteractor().handleFeePerKbValue(feePerKb);

                        minFee = feePerKb.getFeePerKb().doubleValue();
                        getView().updateFee(minFee, maxFee);
                    }
                });
    }

    private SendInteractor getInteractor() {
        return mSendBaseFragmentInteractor;
    }

    @Override
    public void onResponse(String publicAddress, double amount, String tokenAddress) {
        String tokenName = validateTokenExistance(tokenAddress);
        getView().updateData(publicAddress, amount, tokenName);
    }

    private String validateTokenExistance(String tokenAddress) {
        if (getView().isTokenEmpty(tokenAddress)) {
            return "";
        }

        String contractName = getInteractor().validateTokenExistance(tokenAddress);
        if (contractName != null) {
            return contractName;
        }

        getView().setAlertDialog(org.qtum.wallet.R.string.token_not_found, "Ok", BaseFragment.PopUpType.error);
        return "";
    }

    @Override
    public void onResponseError() {
        getView().errorRecognition();
    }

    private String availableAddress = null;
    private String params = null;

    @Override
    public void send(final String from, final String address, final String amount, final Currency currency, String feeString) {
        if (mNetworkConnectedFlag) {
            final double feeDouble = Double.valueOf(feeString);
            if (feeDouble < minFee || feeDouble > maxFee) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(org.qtum.wallet.R.string.error, R.string.invalid_fee, "Ok", BaseFragment.PopUpType.error);
                return;
            }
            final String fee = validateFee(feeDouble);
            if (!getView().isValidAmount(amount)) {
                return;
            }

            PinDialogFragment.PinCallBack callback = new PinDialogFragment.PinCallBack() {
                @Override
                public void onSuccess() {
                    getView().setProgressDialog();
                    if (currency.getName().equals("Qtum " + getView().getStringValue(org.qtum.wallet.R.string.default_currency))) {
                        getInteractor().sendTx(from, address, amount, fee, new SendInteractorImpl.SendTxCallBack() {
                            @Override
                            public void onSuccess() {
                                getView().setAlertDialog(org.qtum.wallet.R.string.payment_completed_successfully, "Ok", BaseFragment.PopUpType.confirm);
                            }

                            @Override
                            public void onError(String error) {
                                getView().dismissProgressDialog();
                                getView().setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
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
                                    getView().setAlertDialog(org.qtum.wallet.R.string.error,
                                            org.qtum.wallet.R.string.you_have_insufficient_funds_for_this_transaction,
                                            "Ok", BaseFragment.PopUpType.error);
                                    return;
                                }

                                getInteractor().createAbiMethodParamsObservable(address, resultAmount, "transfer")
                                        .flatMap(new Func1<String, Observable<CallSmartContractResponse>>() {
                                            @Override
                                            public Observable<CallSmartContractResponse> call(String s) {
                                                params = s;
                                                return getInteractor().callSmartContractObservable(token, s);
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
                                                createTx(params, token.getContractAddress(), availableAddress, /*TODO callSmartContractResponse.getItems().get(0).getGasUsed()*/ 2000000, fee);

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
            };

            getView().showPinDialog(callback);
        } else {
            getView().setAlertDialog(org.qtum.wallet.R.string.no_internet_connection, org.qtum.wallet.R.string.please_check_your_network_settings, "Ok", BaseFragment.PopUpType.error);
        }
    }

    private String validateFee(Double fee) {
        return getInteractor().getValidatedFee(fee);
    }

    private void createTx(final String abiParams, final String contractAddress, String senderAddress, final int gasLimit, final String fee) {
        getInteractor().getUnspentOutputs(senderAddress, new SendInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                String txHex = getInteractor().createTransactionHash(abiParams, contractAddress, unspentOutputs, gasLimit, fee);
                getInteractor().sendTx(txHex,
                        new SendInteractorImpl.SendTxCallBack() {
                            @Override
                            public void onSuccess() {
                                getView().setAlertDialog(org.qtum.wallet.R.string.payment_completed_successfully, "Ok", BaseFragment.PopUpType.confirm);
                            }

                            @Override
                            public void onError(String error) {
                                getView().dismissProgressDialog();
                                getView().setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
                            }
                        });
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
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

    @Override
    public void updateNetworkSate(boolean networkConnectedFlag) {
        mNetworkConnectedFlag = networkConnectedFlag;
    }

}