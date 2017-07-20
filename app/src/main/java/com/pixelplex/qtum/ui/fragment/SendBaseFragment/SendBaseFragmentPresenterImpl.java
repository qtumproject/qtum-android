package com.pixelplex.qtum.ui.fragment.SendBaseFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.listeners.NetworkStateListener;
import com.pixelplex.qtum.dataprovider.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.history.Vin;
import com.pixelplex.qtum.model.gson.history.Vout;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.dataprovider.listeners.TransactionListener;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.model.gson.tokenBalance.TokenBalance;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.CurrencyFragment.CurrencyFragment;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinDialogFragment;
import com.pixelplex.qtum.ui.fragment.SendBaseFragment.QrCodeRecognitionFragment.QrCodeRecognitionFragment;
import com.pixelplex.qtum.utils.ContractBuilder;

import org.bitcoinj.script.Script;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class SendBaseFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SendBaseFragmentPresenter {

    private SendBaseFragmentView mSendBaseFragmentView;
    private SendBaseFragmentInteractorImpl mSendBaseFragmentInteractor;
    private UpdateService mUpdateService;
    private Context mContext;
    private NetworkStateReceiver mNetworkStateReceiver;
    private boolean mNetworkConnectedFlag = false;
    private List<Token> mTokenList;

    private static final int REQUEST_CAMERA = 3;
    private boolean OPEN_QR_CODE_FRAGMENT_FLAG = false;


    SendBaseFragmentPresenterImpl(SendBaseFragmentView sendBaseFragmentView) {
        mSendBaseFragmentView = sendBaseFragmentView;
        mContext = getView().getContext();
        mSendBaseFragmentInteractor = new SendBaseFragmentInteractorImpl(mContext);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mUpdateService = getView().getMainActivity().getUpdateService();
        mUpdateService.addTransactionListener(new TransactionListener() {
            @Override
            public void onNewHistory(History history) {
                calculateChangeInBalance(history,getInteractor().getAddresses());
                if(history.getChangeInBalance().doubleValue()<0){
                    getView().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateAvailableBalance();
                        }
                    });
                } else if(history.getBlockTime()!=null){
                    getView().getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateAvailableBalance();
                        }
                    });
                }
            }

            @Override
            public boolean getVisibility() {
                return false;
            }
        });

        mNetworkStateReceiver  = getView().getMainActivity().getNetworkReceiver();
        mNetworkStateReceiver.addNetworkStateListener(new NetworkStateListener() {

            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                mNetworkConnectedFlag = networkConnectedFlag;
            }
        });

        getView().getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if(requestCode == REQUEST_CAMERA) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        OPEN_QR_CODE_FRAGMENT_FLAG = true;
                    }
                }
            }
        });
    }

    public void onCurrencyChoose(String currency){
        getView().setUpCurrencyField(currency);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().getMainActivity().removePermissionResultListener();
        mUpdateService.removeTransactionListener();
        mNetworkStateReceiver.removeNetworkStateListener();
        //TODO:unsubscribe rx
    }

    @Override
    public SendBaseFragmentView getView() {
        return mSendBaseFragmentView;
    }

    @Override
    public void onClickQrCode() {

        if(getView().getMainActivity().checkPermission(Manifest.permission.CAMERA)){
            openQrCodeFragment();
        } else {
            getView().getMainActivity().loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);
        }
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        if(OPEN_QR_CODE_FRAGMENT_FLAG) {
            openQrCodeFragment();
        }
    }

    private void openQrCodeFragment(){
        OPEN_QR_CODE_FRAGMENT_FLAG = false;
        QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
        getView().hideKeyBoard();
        getView().openInnerFragmentForResult(qrCodeRecognitionFragment);
    }

    private void updateAvailableBalance(){
        getView().setProgressBar();
        getInteractor().getUnspentOutputs(new SendBaseFragmentInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                BigDecimal balance = new BigDecimal("0");
                BigDecimal amount;
                for(UnspentOutput unspentOutput : unspentOutputs){
                    amount = new BigDecimal(String.valueOf(unspentOutput.getAmount()));
                    balance = balance.add(amount);
                }
                getView().updateAvailableBalance(balance.toString() + " QTUM");
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(mContext.getString(R.string.error), error, "Ok", BaseFragment.PopUpType.error);
            }
        });
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        updateAvailableBalance();
        String currency;
        mTokenList = new ArrayList<>();
        for(Token token : getInteractor().getTokenList()){
            if(token.isSubscribe()){
                mTokenList.add(token);
            }
        }
        if(!mTokenList.isEmpty()) {
            currency = "Qtum "+mContext.getString(R.string.default_currency);
            getView().setUpCurrencyField(currency);
        }else {
            getView().hideCurrencyField();
        }
    }

    private SendBaseFragmentInteractorImpl getInteractor() {
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
        tokenAddress = validateTokenExistance(tokenAddress);
        getView().updateData(publicAddress, amount, tokenAddress);
    }

    private String validateTokenExistance(String tokenAddress){

        if(TextUtils.isEmpty(tokenAddress)){
            return "";
        }

        TinyDB tdb = new TinyDB(mContext);
        List<Token> tokenList = tdb.getTokenList();
        for (Token token : tokenList){
            if(tokenAddress.equals(token.getContractAddress())){
                return tokenAddress;
            }
        }
        getView().setAlertDialog("Token not found","Ok", BaseFragment.PopUpType.error);
        return "";
    }

    @Override
    public void onResponseError() {
        getView().errorRecognition();
    }

    @Override
    public void onCurrencyClick() {
        CurrencyFragment currencyFragment = CurrencyFragment.newInstance();
        getView().openFragmentForResult(getView().getFragment(), currencyFragment);
    }

    @Override
    public void send(String[] sendInfo) {

        if(mNetworkConnectedFlag) {
            final String address = sendInfo[0];
            final String amount = sendInfo[1];
            final String currency = sendInfo[2];

            if((TextUtils.isEmpty(amount)) || Float.valueOf(amount) <= 0){
                getView().dismissProgressDialog();
                getView().setAlertDialog(mContext.getString(R.string.error), "Transaction amount can't be zero. Please edit your transaction and try again", "Ok", BaseFragment.PopUpType.error);
                return;
            }

            PinDialogFragment pinDialogFragment = new PinDialogFragment();
            pinDialogFragment.setTouchIdFlag(getView().getMainActivity().checkTouchIdEnable());
            pinDialogFragment.addPinCallBack(new PinDialogFragment.PinCallBack() {
                @Override
                public void onSuccess() {
                    getView().setProgressDialog();
                    if(currency.equals("Qtum "+mContext.getString(R.string.default_currency))) {
                        getInteractor().sendTx(address, amount, new SendBaseFragmentInteractorImpl.SendTxCallBack() {
                            @Override
                            public void onSuccess() {
                                getView().setAlertDialog(mContext.getString(R.string.payment_completed_successfully), "Ok", BaseFragment.PopUpType.confirm);
                            }

                            @Override
                            public void onError(String error) {
                                getView().dismissProgressDialog();
                                getView().setAlertDialog(mContext.getString(R.string.error), error, "Ok", BaseFragment.PopUpType.error);
                            }
                        });
                    } else {
                        for(final Token token : mTokenList){
                            if(token.getContractAddress().equals(currency)) {

                                getView().getSocketService().addTokenBalanceChangeListener(token.getContractAddress(), new TokenBalanceChangeListener() {
                                    @Override
                                    public void onBalanceChange(TokenBalance tokenBalance) {

                                        if (tokenBalance.getMaxBalance() < Float.valueOf(amount)){
                                            getView().dismissProgressDialog();
                                            getView().setAlertDialog(mContext.getString(R.string.error), "You have insufficient funds for this transaction", "Ok", BaseFragment.PopUpType.error);
                                            return;
                                        }

                                        ContractBuilder contractBuilder = new ContractBuilder();
                                        List<ContractMethodParameter> contractMethodParameterList = new ArrayList<>();
                                        ContractMethodParameter contractMethodParameterAddress = new ContractMethodParameter("_to", "address", address);
                                        ContractMethodParameter contractMethodParameterAmount = new ContractMethodParameter("_value", "uint256", amount);
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
                                                        getView().setAlertDialog(mContext.getString(R.string.error), e.getMessage(), "Ok", BaseFragment.PopUpType.error);

                                                    }

                                                    @Override
                                                    public void onNext(String s) {
                                                        createTx(s, token.getContractAddress());
                                                    }
                                                });
                                        return;
                                    }
                                });
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
            getView().setAlertDialog(mContext.getString(R.string.no_internet_connection),mContext.getString(R.string.please_check_your_network_settings),"Ok", BaseFragment.PopUpType.error);
        }
    }

    private void createTx(final String abiParams, final String contractAddress) {
        getInteractor().getUnspentOutputs(new SendBaseFragmentInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                ContractBuilder contractBuilder = new ContractBuilder();
                Script script = contractBuilder.createMethodScript(abiParams, contractAddress);
                getInteractor().sendTx(contractBuilder.createTransactionHash(script, unspentOutputs), new SendBaseFragmentInteractorImpl.SendTxCallBack() {
                    @Override
                    public void onSuccess() {
                        getView().setAlertDialog(mContext.getString(R.string.payment_completed_successfully), "Ok", BaseFragment.PopUpType.confirm);
                    }

                    @Override
                    public void onError(String error) {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog(mContext.getString(R.string.error), error, "Ok", BaseFragment.PopUpType.error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(mContext.getString(R.string.error), error, "Ok", BaseFragment.PopUpType.error);
            }
        });

    }

    private void calculateChangeInBalance(History history, List<String> addresses){
        BigDecimal changeInBalance = calculateVout(history,addresses).subtract(calculateVin(history,addresses));
        history.setChangeInBalance(changeInBalance);
    }

    private BigDecimal calculateVin(History history, List<String> addresses){
        BigDecimal totalVin = new BigDecimal("0.0");
        boolean equals = false;
        for(Vin vin : history.getVin()){
            for(String address : addresses){
                if(vin.getAddress().equals(address)){
                    vin.setOwnAddress(true);
                    equals = true;
                }
            }
        }
        if(equals){
            totalVin = history.getAmount();
        }
        return totalVin;
    }

    private BigDecimal calculateVout(History history, List<String> addresses){
        BigDecimal totalVout = new BigDecimal("0.0");
        for(Vout vout : history.getVout()){
            for(String address : addresses){
                if(vout.getAddress().equals(address)){
                    vout.setOwnAddress(true);
                    totalVout = totalVout.add(vout.getValue());
                }
            }
        }
        return totalVout;
    }

}