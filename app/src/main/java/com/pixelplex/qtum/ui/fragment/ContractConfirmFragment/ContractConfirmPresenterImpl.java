package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.content.Context;
import android.util.Log;

import com.pixelplex.qtum.SmartContractsManager.StorageManager;
import com.pixelplex.qtum.dataprovider.RestAPI.QtumService;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionRequest;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionResponse;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.CurrentNetParams;
import com.pixelplex.qtum.utils.TinyDB;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptChunk;
import org.bitcoinj.script.ScriptOpCodes;
import org.spongycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmPresenterImpl extends BaseFragmentPresenterImpl implements ContractConfirmPresenter {

    ContractConfirmView view;
    ContractConfirmInteractorImpl interactor;
    Context mContext;

    private static final int radix = 16;
    private String hashPattern = "0000000000000000000000000000000000000000000000000000000000000000";
                                //0000000000000000000000000000000000000000000000000000000000000037
                                //0000000000000000000000000000000000000000000000000000000000000002
                                //00000000000000000000000000000000000000000000000000000000000021dc
                                //0000000000000000000000000000000000000000000000000000000000000002
                                //7500000000000000000000000000000000000000000000000000000000000000
                                //000000000000000000000000000000000000000000000000000000000000229c
                                //0000000000000000000000000000000000000000000000000000000000000002
                                //7900000000000000000000000000000000000000000000000000000000000000

    private String resultString = "";
    private String contractName = "";
    private static final String TYPE_INT = "int";
    private static final String TYPE_STRING = "string";

    private List<ContractMethodParameter> params;

    public void setParams(List<ContractMethodParameter> params) {
        this.params = params;
    }

    public List<ContractMethodParameter> getParams() {
        return params;
    }

    public ContractConfirmPresenterImpl(ContractConfirmView view) {
        this.view = view;
        mContext = getView().getContext();
        interactor = new ContractConfirmInteractorImpl();
    }

    public String getResult() {
        return resultString;
    }

    public void confirmContract(final String contractName) {
        getView().onStartTransaction();
        this.contractName = contractName;
        formContract(contractName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorTransaction(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        getUnspentOutputs();
                    }
                });
    }

    public Observable<String> formContract(final String contractName) {

       return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if(params != null) {
                    for (ContractMethodParameter parameter : params) {
                        convertParameter(parameter);
                    }
                }
                resultString = getByteCodeByContractName(contractName) + resultString;
                return resultString;
            }
        });
    }

    private String getByteCodeByContractName(String contractName) {
        return StorageManager.getInstance().readByteCodeContract(mContext, contractName);
    }

    boolean isStringChainNow = false;

    @Override
    public ContractConfirmView getView() {
        return view;
    }

    public void convertParameter(ContractMethodParameter parameter) {

        String _value = parameter.value;

        if(parameter.type.contains(TYPE_INT)){
            isStringChainNow = false;
            resultString += appendNumericPattern(convertToByteCode(Long.valueOf(_value)));
        } else {

            if(!isStringChainNow) {
                int stringChainSize = getStringsChainSize(parameter);
                for (int i = 0; i < stringChainSize; i++){
                    String offset = getStringOffset();
                    resultString += offset;
                }
                isStringChainNow = true;
            }
            resultString += appendStringPattern(convertToByteCode(_value));
        }
    }

    private int getStringsChainSize(ContractMethodParameter parameter) {

        if(params == null || params.size() == 0){
            return 0;
        }

        int index = params.indexOf(parameter);

        if(index == params.size()-1) {
            return 1;
        }

        int chainSize = 0;

        for (int i = index; i<params.size();i++){
            if(params.get(index).type.contains(TYPE_STRING)){
                chainSize++;
            }
        }

        return chainSize;
    }

    private String convertToByteCode(long _value) {
        return Long.toString(_value,radix);
    }

    private static String convertToByteCode(String _value)
    {
        char[] chars = _value.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }


    private String getStringOffset() {
        return appendNumericPattern(convertToByteCode(resultString.length()));
    }

    private String getStringLength(String _value) {
        return appendNumericPattern(convertToByteCode(_value.length()/2));
    }

    private String appendStringPattern(String _value) {

        String fullParameter = "";
        fullParameter += getStringLength(_value); // add string parameter length

        if(_value.length()<=hashPattern.length()) {
            fullParameter += formNotFullString(_value);
        }else {
            int ost = _value.length() % hashPattern.length();
            fullParameter += _value + hashPattern.substring(0,hashPattern.length()-ost);
        }

        return fullParameter;
    }

    private String formNotFullString(String _value) {
        return _value + hashPattern.substring(_value.length());
    }

    private String appendNumericPattern(String _value){
        return hashPattern.substring(0,hashPattern.length()-_value.length()) + _value;
    }

    public void formTransaction(List<UnspentOutput> unspentOutputs){
                final int OP_PUSHDATA_1 = 1;
                final int OP_PUSHDATA_8 = 8;
                final int OP_EXEC = 193;
                final int OP_EXEC_ASSIGN = 194;
                final int OP_EXEC_SPEND = 195;

                byte[] version = Hex.decode("01");
                byte[] gasLimit = Hex.decode("80841e0000000000");
                byte[] gasPrice = Hex.decode("0100000000000000");
                byte[] data = Hex.decode(resultString);
                byte[] program;

                ScriptChunk versionChunk = new ScriptChunk(OP_PUSHDATA_1,version);
                ScriptChunk gasLimitChunk = new ScriptChunk(OP_PUSHDATA_8,gasLimit);
                ScriptChunk gasPriceChunk = new ScriptChunk(OP_PUSHDATA_8,gasPrice);
                ScriptChunk dataChunk = new ScriptChunk(ScriptOpCodes.OP_PUSHDATA2,data);
                ScriptChunk opExecChunk = new ScriptChunk(OP_EXEC, null);
                List<ScriptChunk> chunkList = new ArrayList<>();
                chunkList.add(versionChunk);
                chunkList.add(gasLimitChunk);
                chunkList.add(gasPriceChunk);
                chunkList.add(dataChunk);
                chunkList.add(opExecChunk);

                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    for (ScriptChunk chunk : chunkList) {
                        chunk.write(bos);
                    }
                    program = bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Script script = new Script(program);

                Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
                transaction.addOutput(Coin.ZERO,script);

                UnspentOutput unspentOutput = null;
                for(UnspentOutput unspentOutput1: unspentOutputs) {
                    if(unspentOutput1.getAmount().doubleValue()>1.0) {
                        unspentOutput = unspentOutput1;
                        break;
                    }
                }

                if(unspentOutput == null){
                    getView().onErrorTransaction("Not enough money");
                    return;
                }

                //TODO:test
                BigDecimal bitcoin = new BigDecimal(100000000);
                ECKey myKey = KeyStorage.getInstance().getCurrentKey();
                transaction.addOutput(Coin.valueOf((long)(unspentOutput.getAmount().multiply(bitcoin).subtract(new BigDecimal("10000000")).doubleValue())),
                        myKey.toAddress(CurrentNetParams.getNetParams()));

                for (DeterministicKey deterministicKey : KeyStorage.getInstance().getKeyList(100)) {
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

                sendTx(transactionHex);
    }

    public void getUnspentOutputs() {
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

                        for(Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext();){
                            UnspentOutput unspentOutput = iterator.next();
                            if(unspentOutput.getConfirmations()==0){
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });
                        formTransaction(unspentOutputs);
                    }
                });
    }

    public void sendTx(String code) {
        QtumService.newInstance().sendRawTransaction(new SendRawTransactionRequest(code, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRawTransactionResponse>() {
                    @Override
                    public void onCompleted() {
                        getView().onCompleteTransaction();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorTransaction(e.getMessage());
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        String s = sendRawTransactionResponse.getResult();
                        getView().getApplication().setContractAwait(true);
                        ContractInfo contractInfo = new ContractInfo(null,contractName,null);
                        TinyDB tinyDB = new TinyDB(mContext);
                        ArrayList<ContractInfo> contractInfoList = tinyDB.getListContractInfo();
                        contractInfoList.add(contractInfo);
                        tinyDB.putListContractInfo(contractInfoList);
                    }
                });
    }

}
