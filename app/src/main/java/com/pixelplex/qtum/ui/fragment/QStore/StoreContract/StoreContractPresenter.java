package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import android.text.TextUtils;
import android.widget.Toast;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.listeners.ContractPurchaseListener;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.QStoreStorage;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.gson.SendRawTransactionRequest;
import com.pixelplex.qtum.model.gson.SendRawTransactionResponse;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.model.gson.store.ContractPurchaseResponse;
import com.pixelplex.qtum.model.gson.store.IsPaidResponse;
import com.pixelplex.qtum.model.gson.store.QstoreBuyResponse;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.model.gson.store.QstoreSourceCodeResponse;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.SendFragment.SendFragmentInteractorImpl;
import com.pixelplex.qtum.utils.CurrentNetParams;

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
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.pixelplex.qtum.datastorage.FileStorageManager.HUMANSTANDARDTOKENUUID;
import static com.pixelplex.qtum.datastorage.QStoreStorage.PurchaseItem.NON_PAID_STATUS;
import static com.pixelplex.qtum.datastorage.QStoreStorage.PurchaseItem.PAID_STATUS;
import static com.pixelplex.qtum.datastorage.QStoreStorage.PurchaseItem.PENDING_STATUS;


public class StoreContractPresenter extends BaseFragmentPresenterImpl implements ContractPurchaseListener {

    private StoreContractView view;
    private QstoreContract qstoreContract;
    private String abiString;

    QstoreBuyResponse qstoreBuyResponse;
    QStoreStorage.PurchaseItem purchaseByContractId;

    public QstoreContract getContract(){
        return qstoreContract;
    }

    public StoreContractPresenter(StoreContractView view) {
        this.view = view;
    }

    @Override
    public StoreContractView getView() {
        return view;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().getMainActivity().getUpdateService().setContractPurchaseListener(this);
    }

    @Override
    public void onDestroyView() {
        getView().getMainActivity().getUpdateService().removeContractPurchaseListener();
        super.onDestroyView();
    }

    @Override
    public void onContractPurchased(final ContractPurchaseResponse purchaseData) {
        getView().getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(qstoreContract.id.equals(purchaseData.contractId)) {
                    getView().setContractPayStatus(PAID_STATUS);
                }
            }
        });
    }

    public void getSourceCode(){
        if(purchaseByContractId != null) {
            getView().setProgressDialog();
            QtumService
                    .newInstance()
                    .getSourceCode(getContract().id, purchaseByContractId.requestId, purchaseByContractId.accessToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QstoreSourceCodeResponse>() {
                        @Override
                        public void onCompleted() {
                            getView().dismissProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().dismissProgressDialog();
                            Toast.makeText(getView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(QstoreSourceCodeResponse o) {
                            getView().openAbiViewer(o.sourceCode);
                        }
                    });
        }
    }

    public void checkIsPaid(){
        purchaseByContractId = QStoreStorage.getInstance(getView().getContext()).getPurchaseByContractId(getContract().id);
        if(purchaseByContractId == null){
            getView().setContractPayStatus(NON_PAID_STATUS);
            return;
        }
        getView().setProgressDialog();
        QtumService
                .newInstance()
                .isPaidByRequestId(getContract().id, purchaseByContractId.requestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IsPaidResponse>() {
            @Override
            public void onCompleted() {
                getView().dismissProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                getView().dismissProgressDialog();
                Toast.makeText(getView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                getView().setContractPayStatus(PENDING_STATUS);
            }

            @Override
            public void onNext(IsPaidResponse o) {
                getView().setContractPayStatus((o != null && !TextUtils.isEmpty(o.payedAt))? PAID_STATUS : PENDING_STATUS);
            }
        });
    }

    public void getContractById(String id){
        getView().setProgressDialog();
        if(!TextUtils.isEmpty(id)) {
            QtumService
                    .newInstance()
                    .getContractById(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<QstoreContract>() {
                        @Override
                        public void onCompleted() {
                            getView().dismissProgressDialog();
                            checkIsPaid();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().dismissProgressDialog();
                            Toast.makeText(getView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(QstoreContract qstoreContract) {
                            StoreContractPresenter.this.qstoreContract = qstoreContract;
                            getView().setContractData(qstoreContract);
                        }
                    });
        }
    }

    public void getContractAbiById(String id){

        if(!TextUtils.isEmpty(abiString)){
            getView().openAbiViewer(abiString);
            return;
        }

        getView().setProgressDialog();
        if(!TextUtils.isEmpty(id)) {
            QtumService
                    .newInstance()
                    .getAbiByContractId(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {
                            getView().dismissProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().dismissProgressDialog();
                            Toast.makeText(getView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(Object abi) {
                            abiString = abi.toString();
                            getView().openAbiViewer(abiString);
                        }
                    });
        }
    }

    public void sendBuyRequest(){
        qstoreBuyResponse = null;
        getView().setProgressDialog();
        QtumService
                .newInstance()
                .buyRequest(getContract().id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QstoreBuyResponse>() {
            @Override
            public void onCompleted() {
                payTransaсtion(qstoreBuyResponse.address, String.valueOf(qstoreBuyResponse.amount));
            }

            @Override
            public void onError(Throwable e) {
                getView().dismissProgressDialog();
                Toast.makeText(getView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(QstoreBuyResponse qstoreBuyResponse) {
                StoreContractPresenter.this.qstoreBuyResponse = qstoreBuyResponse;
            }
        });
    }

    private void payTransaсtion(String address, String amount){
        sendTx(address, amount, new SendFragmentInteractorImpl.SendTxCallBack() {
            @Override
            public void onSuccess() {
                QStoreStorage.getInstance(getView().getContext()).addPurchasedItem(getContract().id,qstoreBuyResponse);
                getView().getMainActivity().getUpdateService().subscribeStoreContract(qstoreBuyResponse.requestId);
                getView().dismissProgressDialog();
                getView().setAlertDialog(getView().getContext().getString(R.string.payment_completed_successfully), "Ok", BaseFragment.PopUpType.confirm);
                getView().disablePurchase();

            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(getView().getContext().getString(R.string.error), error, "Ok", BaseFragment.PopUpType.error);
            }
        });
    }

    public void sendTx(String address, String amount, final SendFragmentInteractorImpl.SendTxCallBack callBack) {
        createTx(address, amount, new SendFragmentInteractorImpl.CreateTxCallBack() {
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

    public void sendTx(String txHex, final SendFragmentInteractorImpl.SendTxCallBack callBack){
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

    public void createTx(final String address, final String amountString, final SendFragmentInteractorImpl.CreateTxCallBack callBack) {
        getUnspentOutputs(new SendFragmentInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
                Address addressToSend;
                BigDecimal bitcoin = new BigDecimal(100000000);
                try {
                    addressToSend = Address.fromBase58(CurrentNetParams.getNetParams(), address);
                } catch (AddressFormatException a) {
                    callBack.onError(getView().getContext().getString(R.string.incorrect_address));
                    return;
                }
                ECKey myKey = KeyStorage.getInstance().getCurrentKey();
                BigDecimal amount = new BigDecimal(amountString);
                BigDecimal fee = new BigDecimal("0.1");

                BigDecimal amountFromOutput = new BigDecimal("0.0");
                BigDecimal overFlow = new BigDecimal("0.0");
                transaction.addOutput(Coin.valueOf((long)(amount.multiply(bitcoin).doubleValue())), addressToSend);

                amount = amount.add(fee);

                for (UnspentOutput unspentOutput : unspentOutputs) {
                    overFlow = overFlow.add(unspentOutput.getAmount());
                    if (overFlow.doubleValue() >= amount.doubleValue()) {
                        break;
                    }
                }
                if (overFlow.doubleValue() < amount.doubleValue()) {
                    callBack.onError(getView().getContext().getString(R.string.you_have_insufficient_funds_for_this_transaction));
                    return;
                }
                BigDecimal delivery = overFlow.subtract(amount);
                if (delivery.doubleValue() != 0.0) {
                    transaction.addOutput(Coin.valueOf((long)(delivery.multiply(bitcoin).doubleValue())), myKey.toAddress(CurrentNetParams.getNetParams()));
                }

                for (UnspentOutput unspentOutput : unspentOutputs) {
                    if(unspentOutput.getAmount().doubleValue() != 0.0)
                        for (DeterministicKey deterministicKey : KeyStorage.getInstance().getKeyList(100)) {
                            if (Hex.toHexString(deterministicKey.getPubKeyHash()).equals(unspentOutput.getPubkeyHash())) {
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

                String transactionHex = Hex.toHexString(bytes);

                callBack.onSuccess(transactionHex);
            }

            @Override
            public void onError(String error) {
                callBack.onError(error);
            }
        });
    }

    public void getUnspentOutputs(final SendFragmentInteractorImpl.GetUnspentListCallBack callBack) {
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

                        for(Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext();){
                            UnspentOutput unspentOutput = iterator.next();
                            if(!unspentOutput.isOutputAvailableToPay()/* || unspentOutput.getConfirmations()==0*/){
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


    public void getDetails() {
        String s = FileStorageManager.getInstance().readAbiContract(getView().getContext(), HUMANSTANDARDTOKENUUID);
        getView().openDetails(s);
    }

}
