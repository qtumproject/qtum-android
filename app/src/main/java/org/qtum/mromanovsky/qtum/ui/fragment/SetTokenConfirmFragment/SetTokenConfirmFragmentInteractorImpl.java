package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment;


import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptChunk;
import org.bitcoinj.script.ScriptOpCodes;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.QtumService;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.ByteCode;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.GenerateTokenBytecodeRequest;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionRequest;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import org.qtum.mromanovsky.qtum.datastorage.KeyStorage;
import org.qtum.mromanovsky.qtum.datastorage.QtumToken;
import org.qtum.mromanovsky.qtum.utils.CurrentNetParams;
import org.spongycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class SetTokenConfirmFragmentInteractorImpl implements SetTokenConfirmFragmentInteractor{

    SetTokenConfirmFragmentInteractorImpl(){ }

    @Override
    public void generateTokenBytecode(final GenerateTokenBytecodeCallBack callBack) {
        QtumToken qtumToken = QtumToken.getInstance();
        GenerateTokenBytecodeRequest generateTokenBytecodeRequest = new GenerateTokenBytecodeRequest(qtumToken.getInitialSupply(),qtumToken.getTokenName(),
                qtumToken.getDecimalUnits(),qtumToken.getTokenSymbol());
        QtumService.newInstance().generateTokenBytecode(generateTokenBytecodeRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ByteCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ByteCode byteCode) {
                        callBack.onSuccess(byteCode.getBytecode());
                    }
                });
    }

    public void sendToken(final SendTokenCallBack sendTokenCallBack){
        generateTokenBytecode(new GenerateTokenBytecodeCallBack() {
            @Override
            public void onSuccess(final String byteCode) {

                getUnspentOutputs(new GetUnspentListCallBack() {
                    @Override
                    public void onSuccess(List<UnspentOutput> unspentOutputs) {
                        final int OP_PUSHDATA_1 = 1;
                        final int OP_PUSHDATA_8 = 8;
                        final int OP_EXEC = 193;
                        final int OP_EXEC_ASSIGN = 194;
                        final int OP_EXEC_SPEND = 195;

                        byte[] version = hexStringToByteArray("01");
                        byte[] gasLimit = hexStringToByteArray("0100000000000000");
                        byte[] gasPrice = hexStringToByteArray("0100000000000000");
                        byte[] data = hexStringToByteArray(byteCode);
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

                        UnspentOutput unspentOutput = unspentOutputs.get(0);

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

                        QtumService.newInstance().sendRawTransaction(new SendRawTransactionRequest(transactionHex, 1))
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
                                        sendTokenCallBack.onSuccess();
                                    }
                                });
                    }
                });
            }
        });
    }

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
                        callBack.onSuccess(unspentOutputs);
                    }
                });
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    interface GenerateTokenBytecodeCallBack{
        void onSuccess(String byteCode);
    }

    interface GetUnspentListCallBack {
        void onSuccess(List<UnspentOutput> unspentOutputs);
    }

    interface SendTokenCallBack{
        void onSuccess();
    }
}
