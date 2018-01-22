package org.qtum.wallet.ui.fragment.send_fragment;

import android.content.Context;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.script.Script;
import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.CallSmartContractRequest;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.utils.ContractBuilder;
import org.qtum.wallet.utils.CurrentNetParams;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SendInteractorImpl implements SendInteractor {

    private Context mContext;

    public SendInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public BigDecimal getFeePerKb() {
        QtumSettingSharedPreference qtumSettingSharedPreference = new QtumSettingSharedPreference();
        return new BigDecimal(qtumSettingSharedPreference.getFeePerKb(mContext));
    }

    @Override
    public void getUnspentOutputs(final GetUnspentListCallBack callBack) {
        QtumService.newInstance().getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses())
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

                        for (Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext(); ) {
                            UnspentOutput unspentOutput = iterator.next();
                            if (!unspentOutput.isOutputAvailableToPay()/* || unspentOutput.getConfirmations()==0*/) {
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

    public void getUnspentOutputs(String address, final GetUnspentListCallBack callBack) {
        if (address.equals("")) {
            getUnspentOutputs(callBack);
            return;
        }
        QtumService.newInstance().getUnspentOutputs(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {

                        for (Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext(); ) {
                            UnspentOutput unspentOutput = iterator.next();
                            if (!unspentOutput.isOutputAvailableToPay()) {
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

    @Override
    public void createTx(final String from, final String address, final String amountString, final String feeString, final BigDecimal estimateFeePerKb, final CreateTxCallBack callBack) {
        getUnspentOutputs(from, new GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
                Address addressToSend;
                BigDecimal bitcoin = new BigDecimal(100000000);
                try {
                    addressToSend = Address.fromBase58(CurrentNetParams.getNetParams(), address);
                } catch (AddressFormatException a) {
                    callBack.onError(mContext.getString(org.qtum.wallet.R.string.invalid_qtum_address));
                    return;
                }
                ECKey myKey = KeyStorage.getInstance().getCurrentKey();
                BigDecimal amount = new BigDecimal(amountString);
                BigDecimal fee = new BigDecimal(feeString);
                BigDecimal amountFromOutput = new BigDecimal("0.0");
                BigDecimal overFlow = new BigDecimal("0.0");
                transaction.addOutput(Coin.valueOf((long) (amount.multiply(bitcoin).doubleValue())), addressToSend);
                amount = amount.add(fee);

                for (UnspentOutput unspentOutput : unspentOutputs) {
                    overFlow = overFlow.add(unspentOutput.getAmount());
                    if (overFlow.doubleValue() >= amount.doubleValue()) {
                        break;
                    }
                }
                if (overFlow.doubleValue() < amount.doubleValue()) {
                    callBack.onError(mContext.getString(org.qtum.wallet.R.string.you_have_insufficient_funds_for_this_transaction));
                    return;
                }
                BigDecimal delivery = overFlow.subtract(amount);
                if (delivery.doubleValue() != 0.0) {
                    transaction.addOutput(Coin.valueOf((long) (delivery.multiply(bitcoin).doubleValue())), from.isEmpty() ? myKey.toAddress(CurrentNetParams.getNetParams()) : new Address(CurrentNetParams.getNetParams(), from));
                }
                for (UnspentOutput unspentOutput : unspentOutputs) {
                    for (DeterministicKey deterministicKey : KeyStorage.getInstance().getKeyList()) {
                        if (deterministicKey.toAddress(CurrentNetParams.getNetParams()).toString().equals(unspentOutput.getAddress())) {
                            Sha256Hash sha256Hash = new Sha256Hash(Utils.parseAsHexOrBase58(unspentOutput.getTxHash()));
                            TransactionOutPoint outPoint = new TransactionOutPoint(CurrentNetParams.getNetParams(), unspentOutput.getVout(), sha256Hash);
                            Script script = new Script(Utils.parseAsHexOrBase58(unspentOutput.getTxoutScriptPubKey()));
                            transaction.addSignedInput(outPoint, script, deterministicKey, Transaction.SigHash.ALL, true);
                            amountFromOutput = amountFromOutput.add(unspentOutput.getAmount());
                            break;
                        }
                    }
                    if (amountFromOutput.doubleValue() >= amount.doubleValue()) {
                        break;
                    }
                }
                transaction.getConfidence().setSource(TransactionConfidence.Source.SELF);
                transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);
                byte[] bytes = transaction.unsafeBitcoinSerialize();
                int txSizeInkB = (int) Math.ceil(bytes.length / 1024.);
                BigDecimal minimumFee = (estimateFeePerKb.multiply(new BigDecimal(txSizeInkB)));
                if (minimumFee.doubleValue() > fee.doubleValue()) {
                    callBack.onError(mContext.getString(R.string.insufficient_fee_lease_use_minimum_of) + " " + minimumFee.toString() + " QTUM");
                    return;
                }
                String transactionHex = Hex.toHexString(bytes);
                callBack.onSuccess(transactionHex);
            }

            @Override
            public void onError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void sendTx(final String from, final String address, final String amount, final String fee, final SendTxCallBack callBack) {
        createTx(from, address, amount, fee, getFeePerKb(), new CreateTxCallBack() {
            @Override
            public void onSuccess(String txHex) {
                sendTx(txHex, callBack);
            }

            @Override
            public void onError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void sendTx(String txHex, final SendTxCallBack callBack) {
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

    public interface GetUnspentListCallBack {
        void onSuccess(List<UnspentOutput> unspentOutputs);

        void onError(String error);
    }

    public interface CreateTxCallBack {
        void onSuccess(String txHex);

        void onError(String error);
    }

    public interface SendTxCallBack {
        void onSuccess();

        void onError(String error);
    }

    @Override
    public List<String> getAddresses() {
        return KeyStorage.getInstance().getAddresses();
    }

    @Override
    public List<Token> getTokenList() {
        return (new TinyDB(mContext).getTokenList());
    }

    @Override
    public String validateTokenExistance(String tokenAddress) {
        TinyDB tdb = new TinyDB(mContext);
        List<Token> tokenList = tdb.getTokenList();
        for (Token token : tokenList) {
            if (tokenAddress.equals(token.getContractAddress())) {
                return token.getContractName();
            }
        }
        return null;
    }

    @Override
    public String getValidatedFee(Double fee) {
        String pattern = "##0.00000000";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(fee);
    }

    @Override
    public String createTransactionHash(String abiParams, String contractAddress, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee) {
        ContractBuilder contractBuilder = new ContractBuilder();
        Script script = contractBuilder.createMethodScript(abiParams, gasLimit, gasPrice, contractAddress);
        return contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice, getFeePerKb(), fee, "",mContext);
    }

    @Override
    public Observable<String> createAbiMethodParamsObservable(String address, String resultAmount, String transfer) {
        ContractBuilder contractBuilder = new ContractBuilder();
        List<ContractMethodParameter> contractMethodParameterList = new ArrayList<>();
        ContractMethodParameter contractMethodParameterAddress = new ContractMethodParameter("_to", "address", address);

        ContractMethodParameter contractMethodParameterAmount = new ContractMethodParameter("_value", "uint256", resultAmount);
        contractMethodParameterList.add(contractMethodParameterAddress);
        contractMethodParameterList.add(contractMethodParameterAmount);
        return contractBuilder.createAbiMethodParams("transfer", contractMethodParameterList);
    }

    @Override
    public Observable<CallSmartContractResponse> callSmartContractObservable(Token token, String hash, String fromAddress) {
        return QtumService.newInstance().callSmartContract(token.getContractAddress(), new CallSmartContractRequest(new String[]{hash}, fromAddress));
    }

    @Override
    public int getMinGasPrice() {
        return QtumSharedPreference.getInstance().getMinGasPrice(mContext);
    }
}