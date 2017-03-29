package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;

import android.content.Context;
import android.util.Log;

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
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionRequest;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import org.qtum.mromanovsky.qtum.datastorage.HistoryList;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.utils.CurrentNetParams;
import org.spongycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class SendBaseFragmentInteractorImpl implements SendBaseFragmentInteractor {

    private Context mContext;

    SendBaseFragmentInteractorImpl(Context context) {
        mContext = context;
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
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {
                        callBack.onSuccess(unspentOutputs);
                    }
                });
    }

    @Override
    public void createTx(final String address, final String amountString, final CreateTxCallBack callBack) {
        getUnspentOutputs(new GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
                Address addressToSend = null;
                try {
                    addressToSend = Address.fromBase58(CurrentNetParams.getNetParams(), address);
                } catch (AddressFormatException a) {
                    callBack.onError("Incorrect Address");
                }
                ECKey ecKey = KeyStorage.getInstance().getCurrentKey();
                double amount = Double.parseDouble(amountString) / (QtumSharedPreference.getInstance().getExchangeRates(mContext));
                double fee = 0.00001;


                Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                    @Override
                    public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                        return unspentOutput.getAmount() > t1.getAmount() ? 1 : (unspentOutput.getAmount() < t1.getAmount()) ? -1 : 0;
                    }
                });

                double amountFromOutput = 0.0;
                double overFlow = 0.0;
                if (addressToSend != null) {
                    transaction.addOutput(Coin.valueOf((long)(amount*100000000)), addressToSend);
                }

                amount += fee;

                for (UnspentOutput unspentOutput : unspentOutputs) {
                    overFlow += unspentOutput.getAmount();
                    if (overFlow >= amount) {
                        break;
                    }
                }
                if (overFlow < amount) {
                    //TODO: throw exception
                    callBack.onError("Not enough money");
                    return;
                }
                double delivery = overFlow - amount;
                if (delivery != 0.0) {
                    transaction.addOutput(Coin.valueOf((long)(delivery*100000000)), ecKey.toAddress(CurrentNetParams.getNetParams()));
                }

                for (UnspentOutput unspentOutput : unspentOutputs) {
                    if(unspentOutput.getAmount() != 0.0)
                    for (DeterministicKey deterministicKey : KeyStorage.getInstance().getKeyList()) {
                        if (Hex.toHexString(deterministicKey.getPubKeyHash()).equals(unspentOutput.getPubkeyHash())) {
                            Sha256Hash sha256Hash = new Sha256Hash(Utils.parseAsHexOrBase58(unspentOutput.getTxHash()));
                            TransactionOutPoint outPoint = new TransactionOutPoint(CurrentNetParams.getNetParams(), unspentOutput.getVout(), sha256Hash);
                            Script script = new Script(Utils.parseAsHexOrBase58(unspentOutput.getTxoutScriptPubKey()));
                            transaction.addSignedInput(outPoint, script, deterministicKey, Transaction.SigHash.ALL, true);
                            amountFromOutput += unspentOutput.getAmount();
                            break;
                        }
                    }
                    if (amountFromOutput >= amount) {
                        break;
                    }
                }


                transaction.getConfidence().setSource(TransactionConfidence.Source.SELF);
                transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);

                byte[] bytes = transaction.unsafeBitcoinSerialize();

                String transactionHex = Hex.toHexString(bytes);

                Date date = new Date();
                long l = date.getTime() / 1000;
                int i3 = (int) l;
                byte[] bytesData = ByteBuffer.allocate(4).putInt(i3).array();
                byte tmp1 = bytesData[3];
                byte tmp2 = bytesData[2];
                byte tmp3 = bytesData[1];
                byte tmp4 = bytesData[0];
                bytesData[0] = tmp1;
                bytesData[1] = tmp2;
                bytesData[2] = tmp3;
                bytesData[3] = tmp4;

                transactionHex += Hex.toHexString(bytesData);
                callBack.onSuccess(transactionHex);
            }
        });
    }

    @Override
    public void sendTx(String address, String amount, final SendTxCallBack callBack) {
        createTx(address, amount, new CreateTxCallBack() {
            @Override
            public void onSuccess(String txHex) {
                QtumService.newInstance().sendRawTransaction(new SendRawTransactionRequest(txHex, 1))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Void aVoid) {
                                callBack.onSuccess();
                            }
                        });
            }

            @Override
            public void onError(String error) {
                callBack.onError(error);
            }
        });
    }

    interface GetUnspentListCallBack {
        void onSuccess(List<UnspentOutput> unspentOutputs);
    }

    interface CreateTxCallBack {
        void onSuccess(String txHex);

        void onError(String error);
    }

    interface SendTxCallBack {
        void onSuccess();

        void onError(String error);
    }

    @Override
    public int getPassword() {
        return QtumSharedPreference.getInstance().getWalletPassword(mContext);
    }

    @Override
    public double getBalance() {
        return HistoryList.getInstance().getBalance() * QtumSharedPreference.getInstance().getExchangeRates(mContext);
    }
}