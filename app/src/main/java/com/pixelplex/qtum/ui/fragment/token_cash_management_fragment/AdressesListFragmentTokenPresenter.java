package com.pixelplex.qtum.ui.fragment.token_cash_management_fragment;

import android.content.Context;
import android.text.TextUtils;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.services.update_service.listeners.TokenBalanceChangeListener;
import com.pixelplex.qtum.dataprovider.rest_api.QtumService;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.model.gson.SendRawTransactionRequest;
import com.pixelplex.qtum.model.gson.SendRawTransactionResponse;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.model.gson.token_balance.Balance;
import com.pixelplex.qtum.model.gson.token_balance.TokenBalance;
import com.pixelplex.qtum.ui.fragment.qtum_cash_management_fragment.AddressListFragmentPresenter;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.send_fragment.SendFragmentInteractorImpl;
import com.pixelplex.qtum.utils.ContractBuilder;
import com.pixelplex.qtum.utils.CurrentNetParams;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.script.Script;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public class AdressesListFragmentTokenPresenter extends BaseFragmentPresenterImpl implements Runnable {

    AdressesListFragmentTokenView view;
    public List<DeterministicKeyWithTokenBalance> items = new ArrayList<>();
    private Token token;
    private String currency;
    List<DeterministicKey> addrs;
    public DeterministicKeyWithTokenBalance keyWithTokenBalanceFrom;

    TokenBalance tokenBalance;

    public AdressesListFragmentTokenPresenter(AdressesListFragmentTokenView view){
        this.view = view;
    }

    @Override
    public AdressesListFragmentTokenView getView() {
        return view;
    }

    public List<DeterministicKey> getAdresses(){
        if(addrs == null){
            addrs = KeyStorage.getInstance().getKeyList(10);
        }
        return addrs;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

        getView().getSocketInstance().addTokenBalanceChangeListener(token.getContractAddress(), new TokenBalanceChangeListener() {
            @Override
            public void onBalanceChange(TokenBalance tokenBalance) {
                getView().getSocketInstance().removeTokenBalanceChangeListener(tokenBalance.getContractAddress());
                AdressesListFragmentTokenPresenter.this.tokenBalance = tokenBalance;
                processTokenBalances(tokenBalance);
                getView().getHandler().post(AdressesListFragmentTokenPresenter.this);
            }
        });

    }

    @Override
    public void onPause(Context context) {
        getView().getHandler().removeCallbacks(this);
        super.onPause(context);
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
    }

    private void processTokenBalances(TokenBalance balance){
        for (DeterministicKey item : getAdresses()){
            DeterministicKeyWithTokenBalance deterministicKeyWithTokenBalance = new DeterministicKeyWithTokenBalance(item);
            items.add(deterministicKeyWithTokenBalance);
            processTokenBalace(deterministicKeyWithTokenBalance, balance);
        }
    }

    private void processTokenBalace(DeterministicKeyWithTokenBalance deterministicKeyWithTokenBalance, TokenBalance balance){
        for (Balance bal : balance.getBalances()){
            if(deterministicKeyWithTokenBalance.getAddress().equals(bal.getAddress())){
                deterministicKeyWithTokenBalance.addBalance(BigDecimal.valueOf(bal.getBalance()));
            }
        }
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setCurrency(String currency){
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }


    @Override
    public void run() {
        if(items != null && !TextUtils.isEmpty(currency)) {
            getView().updateAddressList(items, currency);
        }
    }

    public void transfer(DeterministicKeyWithTokenBalance keyWithBalanceTo, final DeterministicKeyWithTokenBalance keyWithTokenBalanceFrom, String amountString, final AddressListFragmentPresenter.TransferListener transferListener) {

        if(TextUtils.isEmpty(amountString)){
            getView().setAlertDialog(getView().getContext().getResources().getString(R.string.error),
                    getView().getContext().getResources().getString(R.string.enter_valid_amount_value),
                    getView().getContext().getResources().getString(R.string.ok),
                    BaseFragment.PopUpType.error);
            return;
        }

        if(Float.valueOf(amountString) <= 0){
            getView().setAlertDialog(getView().getContext().getResources().getString(R.string.error),
                    getView().getContext().getResources().getString(R.string.transaction_amount_cant_be_zero),
                    getView().getContext().getResources().getString(R.string.ok),
                    BaseFragment.PopUpType.error);
            return;
        }

        getView().hideTransferDialog();

        if (tokenBalance == null || tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()) == null || tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()).getBalance() < Float.valueOf(amountString)){
            getView().dismissProgressDialog();
            getView().setAlertDialog(getView().getContext().getString(R.string.error), getView().getContext().getString(R.string.you_have_insufficient_funds_for_this_transaction), "Ok", BaseFragment.PopUpType.error);
            return;
        }

        ContractBuilder contractBuilder = new ContractBuilder();
        List<ContractMethodParameter> contractMethodParameterList = new ArrayList<>();
        ContractMethodParameter contractMethodParameterAddressTo = new ContractMethodParameter("_to", "address", keyWithBalanceTo.getAddress());
        ContractMethodParameter contractMethodParameterAmount = new ContractMethodParameter("_value", "uint256", amountString);
        contractMethodParameterList.add(contractMethodParameterAddressTo);
        contractMethodParameterList.add(contractMethodParameterAmount);
        contractBuilder.createAbiMethodParams("transfer", contractMethodParameterList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        transferListener.onError(e.getMessage());

                    }

                    @Override
                    public void onNext(String s) {
                        createTx(s, token.getContractAddress(), keyWithTokenBalanceFrom.getAddress());
                    }
                });

    }

    private void createTx(final String abiParams, final String contractAddress, String senderAddress) {
        getUnspentOutputs(senderAddress, new SendFragmentInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {

                ContractBuilder contractBuilder = new ContractBuilder();
                Script script = contractBuilder.createMethodScript(abiParams, contractAddress);
                sendTx(createTransactionHash(script, unspentOutputs), new SendFragmentInteractorImpl.SendTxCallBack() {
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

    public String createTransactionHash(Script script, List<UnspentOutput> unspentOutputs) {

        Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
        transaction.addOutput(Coin.ZERO, script);

        UnspentOutput unspentOutput = null;

        for (UnspentOutput output : unspentOutputs){
            if (output.getAmount().doubleValue() > 1.0) {
                unspentOutput = output;
                break;
            }
        }

        if (unspentOutput == null) {
            throw new RuntimeException("You have insufficient funds for this transaction");
        }

        BigDecimal bitcoin = new BigDecimal(100000000);
        ECKey myKey = keyWithTokenBalanceFrom.getKey();
        transaction.addOutput(Coin.valueOf((long) (unspentOutput.getAmount().multiply(bitcoin).subtract(new BigDecimal("10000000")).doubleValue())),
                myKey.toAddress(CurrentNetParams.getNetParams()));

        for (DeterministicKey deterministicKey : KeyStorage.getInstance().getKeyList(10)) {
            if (Hex.toHexString(deterministicKey.getPubKeyHash()).equals(unspentOutput.getPubkeyHash())) {
                Sha256Hash sha256Hash = new Sha256Hash(Utils.parseAsHexOrBase58(unspentOutput.getTxHash()));
                TransactionOutPoint outPoint = new TransactionOutPoint(CurrentNetParams.getNetParams(), unspentOutput.getVout(), sha256Hash);
                Script script2 = new Script(Utils.parseAsHexOrBase58(unspentOutput.getTxoutScriptPubKey()));
                transaction.addSignedInput(outPoint, script2, deterministicKey, Transaction.SigHash.ALL, true);
                break;
            }
        }

        transaction.getConfidence().setSource(TransactionConfidence.Source.SELF);
        transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);

        byte[] bytes = transaction.unsafeBitcoinSerialize();
        return Hex.toHexString(bytes);
    }

    private void getUnspentOutputs(String address, final SendFragmentInteractorImpl.GetUnspentListCallBack callBack) {
        QtumService.newInstance().getUnspentOutputs(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError("Get Unspent Outputs " + e.getMessage());
                    }
                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {

                        for(Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext();){
                            UnspentOutput unspentOutput = iterator.next();
                            if(!unspentOutput.isOutputAvailableToPay()){
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });
                        callBack.onSuccess(unspentOutputs);
                    }
                });
    }

    private void sendTx(String txHex, final SendFragmentInteractorImpl.SendTxCallBack callBack){
        QtumService.newInstance().sendRawTransaction(new SendRawTransactionRequest(txHex, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRawTransactionResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        callBack.onSuccess();
                    }
                });
    }
}
