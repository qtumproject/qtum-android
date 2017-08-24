package com.pixelplex.qtum.ui.fragment.SendFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.services.update_service.listeners.BalanceChangeListener;
import com.pixelplex.qtum.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import com.pixelplex.qtum.dataprovider.services.update_service.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.history.Vin;
import com.pixelplex.qtum.model.gson.history.Vout;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.dataprovider.services.update_service.UpdateService;
import com.pixelplex.qtum.model.gson.token_balance.Balance;
import com.pixelplex.qtum.model.gson.token_balance.TokenBalance;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.CurrencyFragment.CurrencyFragment;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinDialogFragment;
import com.pixelplex.qtum.ui.fragment.SendFragment.QrCodeRecognitionFragment.QrCodeRecognitionFragment;
import com.pixelplex.qtum.utils.ContractBuilder;
import org.bitcoinj.script.Script;
import java.math.BigDecimal;
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
        getView().getMainActivity().subscribeServiceConnectionChangeEvent(new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if(isConnecting){
                    mUpdateService = getView().getMainActivity().getUpdateService();
                    mUpdateService.removeBalanceChangeListener();

                    mUpdateService.addBalanceChangeListener(new BalanceChangeListener() {
                        @Override
                        public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
                            getView().getMainActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String balanceString = balance.toString();
                                    if(balanceString!=null) {
                                        String unconfirmedBalanceString = unconfirmedBalance.toString();
                                        if(!TextUtils.isEmpty(unconfirmedBalanceString) && !unconfirmedBalanceString.equals("0")) {
                                            getView().updateBalance(String.format("%S QTUM", balanceString),String.format("%S QTUM", String.valueOf(unconfirmedBalance.floatValue())));
                                        } else {
                                            getView().updateBalance(String.format("%S QTUM", balanceString),null);
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
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

    @Override
    public void initializeViews() {
        super.initializeViews();
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
        getView().setAlertDialog(mContext.getString(R.string.token_not_found),"Ok", BaseFragment.PopUpType.error);
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
    public void send(String[] sendInfo) {

        if(mNetworkConnectedFlag) {
            final String address = sendInfo[0];
            final String amount = sendInfo[1];
            final String currency = sendInfo[2];

            if((TextUtils.isEmpty(amount)) || Float.valueOf(amount) <= 0){
                getView().dismissProgressDialog();
                getView().setAlertDialog(mContext.getString(R.string.error), mContext.getResources().getString(R.string.transaction_amount_cant_be_zero), "Ok", BaseFragment.PopUpType.error);
                return;
            }

            PinDialogFragment pinDialogFragment = new PinDialogFragment();
            pinDialogFragment.setTouchIdFlag(getView().getMainActivity().checkTouchIdEnable());
            pinDialogFragment.addPinCallBack(new PinDialogFragment.PinCallBack() {
                @Override
                public void onSuccess() {
                    getView().setProgressDialog();
                    if(currency.equals("Qtum "+mContext.getString(R.string.default_currency))) {
                        getInteractor().sendTx(address, amount, new SendFragmentInteractorImpl.SendTxCallBack() {
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
                            if(token.getContractName().equals(currency)) {

                                getView().getSocketService().addTokenBalanceChangeListener(token.getContractAddress(), new TokenBalanceChangeListener() {
                                    @Override
                                    public void onBalanceChange(TokenBalance tokenBalance) {

                                        availableAddress = null;

                                        for (Balance balance : tokenBalance.getBalances()){
                                            if(balance.getBalance() >= Float.valueOf(amount)){
                                                availableAddress = balance.getAddress();
                                                break;
                                            }
                                        }

                                        if (TextUtils.isEmpty(availableAddress)){
                                            getView().dismissProgressDialog();
                                            getView().setAlertDialog(mContext.getString(R.string.error), mContext.getString(R.string.you_have_insufficient_funds_for_this_transaction), "Ok", BaseFragment.PopUpType.error);
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
                                                        createTx(s, token.getContractAddress(), availableAddress);
                                                    }
                                                });
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

    private void createTx(final String abiParams, final String contractAddress, String senderAddress) {
        getInteractor().getUnspentOutputs(senderAddress, new SendFragmentInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {

                ContractBuilder contractBuilder = new ContractBuilder();
                Script script = contractBuilder.createMethodScript(abiParams, contractAddress);
                getInteractor().sendTx(contractBuilder.createTransactionHash(script, unspentOutputs), new SendFragmentInteractorImpl.SendTxCallBack() {
                    @Override
                    public void onSuccess() {
                        getView().setAlertDialog(getView().getContext().getString(R.string.payment_completed_successfully), "Ok", BaseFragment.PopUpType.confirm);
                    }

                    @Override
                    public void onError(String error) {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog(getView().getContext().getString(R.string.error), error, "Ok", BaseFragment.PopUpType.error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(getView().getContext().getString(R.string.error), error, "Ok", BaseFragment.PopUpType.error);
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