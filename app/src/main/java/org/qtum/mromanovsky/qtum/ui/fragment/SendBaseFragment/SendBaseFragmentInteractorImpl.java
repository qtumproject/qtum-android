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
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.qtum.mromanovsky.qtum.btc.BTCUtils;
import org.qtum.mromanovsky.qtum.btc.UnspentOutputInfo;
import org.qtum.mromanovsky.qtum.dataprovider.jsonrpc.QtumJSONRPCClientImpl;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.model.UnspentOutputResponse;
import org.qtum.mromanovsky.qtum.utils.CurrentNetParams;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SendBaseFragmentInteractorImpl implements SendBaseFragmentInteractor {

    private Context mContext;
    QtumJSONRPCClientImpl mQtumJSONRPCClient = new QtumJSONRPCClientImpl();

    public SendBaseFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public void getUnspentOutputList(final GetUnspentListCallBack callBack) {
        mQtumJSONRPCClient.getUnspentOutputInfo(KeyStorage.getInstance(mContext).getWallet().currentReceiveAddress().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutputResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UnspentOutputResponse> unspentOutputResponses) {
                        callBack.onSuccess(unspentOutputResponses);
                    }
                });
    }

    @Override
    public void createTx(final String address, final String amount, final CreateTxCallBack callBack) {
        getUnspentOutputList(new GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutputResponse> unspentOutputResponseList) {
                //org.bitcoinj.core.Context context = new org.bitcoinj.core.Context(CurrentNetParams.getNetParams());
                Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
                Address addressToSend=null;
                try {
                    addressToSend = Address.fromBase58(CurrentNetParams.getNetParams(), address);
                }catch (AddressFormatException a){
                    callBack.onError("Incorrect Address");
                }
                ECKey ecKey = KeyStorage.getInstance(mContext).getWallet().currentReceiveKey();
                double amountDouble = Double.parseDouble(amount);
                long amountLong =(long) amountDouble * 100000000;


                Collections.sort(unspentOutputResponseList, new Comparator<UnspentOutputResponse>() {
                    @Override
                    public int compare(UnspentOutputResponse unspentOutputResponse, UnspentOutputResponse t1) {
                        return unspentOutputResponse.getAmount() > t1.getAmount() ? 1 : (unspentOutputResponse.getAmount() < t1.getAmount() ) ? -1 : 0;
                    }
                });

                double amountFromOutput = 0;
                double balance = 0;
                transaction.addOutput(Coin.valueOf(amountLong),addressToSend);
                for(UnspentOutputResponse unspentOutputResponse : unspentOutputResponseList){
                    balance += unspentOutputResponse.getAmount();
                    if(amountFromOutput<amountDouble){
                        Sha256Hash sha256Hash = new Sha256Hash((BTCUtils.fromHex(unspentOutputResponse.getTxid())));
                        TransactionOutPoint outPoint = new TransactionOutPoint(CurrentNetParams.getNetParams(), unspentOutputResponse.getVout(), sha256Hash);
                        Script script = new Script(BTCUtils.fromHex(unspentOutputResponse.getScriptPubKey()));
                        transaction.addSignedInput(outPoint, script, ecKey, Transaction.SigHash.ALL, true);
                        amountFromOutput += unspentOutputResponse.getAmount();
                    }
                }
                if(amountFromOutput<amountDouble){
                    //TODO: throw exception
                    callBack.onError("not enough money");
                    return;
                }
                double deliveryDouble = balance - amountDouble;
                long deliveryLong = (long) deliveryDouble*100000000;

                transaction.addOutput(Coin.valueOf(deliveryLong),ecKey.toAddress(CurrentNetParams.getNetParams()));

                transaction.getConfidence().setSource(TransactionConfidence.Source.SELF);
                transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);

                byte[] bytes = transaction.unsafeBitcoinSerialize();

                String transactionHex = BTCUtils.toHex(bytes);
                Date date = new Date();
                long l =  date.getTime()/1000;
                int i3 = (int) l;
                byte[] bytesData = ByteBuffer.allocate(4).putInt(i3).array();
                byte tmp1 = bytesData[3];
                byte tmp2 = bytesData[2];
                byte tmp3 = bytesData[1];
                byte tmp4 = bytesData[0];
                bytesData[0] = tmp4;
                bytesData[1] = tmp3;
                bytesData[2] = tmp2;
                bytesData[3] = tmp1;

                transactionHex+=BTCUtils.toHex(bytesData);
                callBack.onSuccess(transactionHex);
            }
        });
    }

    @Override
    public void sendTx(String address, String amount, final SendTxCallBack callBack) {
        createTx(address, amount, new CreateTxCallBack() {
            @Override
            public void onSuccess(String txHex) {
                mQtumJSONRPCClient.sendTx(txHex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                callBack.onError("Sending Error");
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
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

    public interface GetUnspentListCallBack{
        void onSuccess(List<UnspentOutputResponse> unspentOutputResponseList);
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
    public int getPassword() {
        return QtumSharedPreference.getInstance().getWalletPassword(mContext);
    }
}