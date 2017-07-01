package com.pixelplex.qtum.utils;

import android.content.Context;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.utils.sha3.sha.Keccak;
import com.pixelplex.qtum.utils.sha3.sha.Parameters;

import org.bitcoinj.core.Base58;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;


public class ContractBuilder {

    private boolean isStringChainNow = false;
    private String hashPattern = "0000000000000000000000000000000000000000000000000000000000000000";
    private final int radix = 16;
    private final String TYPE_INT = "int";
    private final String TYPE_STRING = "string";
    private final String TYPE_ADDRESS = "address";

    final int OP_PUSHDATA_1 = 1;
    final int OP_PUSHDATA_8 = 8;
    final int OP_EXEC = 193;
    final int OP_EXEC_ASSIGN = 194;
    final int OP_EXEC_SPEND = 195;

    private Context mContext;

    public ContractBuilder(){

    }

    private List<ContractMethodParameter> mContractMethodParameterList;

    public Observable<String> createAbiMethodParams(final String _methodName, final List<ContractMethodParameter> contractMethodParameterList){
        return rx.Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String methodName = _methodName;
                String parameters = "";
                String abiParams = "";
                mContractMethodParameterList = contractMethodParameterList;
                if(contractMethodParameterList != null && contractMethodParameterList.size()!=0) {
                    for (ContractMethodParameter parameter : contractMethodParameterList) {
                        abiParams += convertParameter(parameter,abiParams);
                        parameters = parameters + parameter.getType() + ",";
                    }
                    methodName = methodName + "("+parameters.substring(0,parameters.length()-1)+")";
                } else{
                    methodName = methodName + "()";
                }
                Keccak keccak = new Keccak();
                String hashMethod = keccak.getHash(Hex.toHexString((methodName).getBytes()), Parameters.KECCAK_256).substring(0,8);
                abiParams = hashMethod + abiParams;
                return abiParams;
            }
        });
    }

    public Observable<String> createAbiConstructParams(final List<ContractMethodParameter> contractMethodParameterList, final long uiid, Context context){
        mContext = context;
        return rx.Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String abiParams = "";
                mContractMethodParameterList = contractMethodParameterList;
                if(mContractMethodParameterList != null) {
                    for (ContractMethodParameter parameter : mContractMethodParameterList) {
                        abiParams += convertParameter(parameter, abiParams);
                    }
                }
                abiParams = getByteCodeByUiid(uiid) + abiParams;
                return abiParams;
            }
        });
    }

    private String convertParameter(ContractMethodParameter parameter, String abiParams) {

        String _value = parameter.getValue();

        if(parameter.getType().contains(TYPE_INT)){
            isStringChainNow = false;
            return appendNumericPattern(convertToByteCode(Long.valueOf(_value)));
        } else if(parameter.getType().contains(TYPE_STRING)){
            String offsetToAbi = "";
            if(!isStringChainNow) {
                int stringChainSize = getStringsChainSize(parameter);
                for (int i = 0; i < stringChainSize; i++){
                    String offset = getStringOffset(abiParams);
                    offsetToAbi += offset;
                    abiParams += offset;
                }
                isStringChainNow = true;
            }
            return  offsetToAbi + appendStringPattern(convertToByteCode(_value));
        } else if(parameter.getType().contains(TYPE_ADDRESS)){
            return appendAddressPattern(Hex.toHexString(Base58.decode(_value)).substring(2,42));
        }
        return "";
    }

    private int getStringsChainSize(ContractMethodParameter parameter) {

        if(mContractMethodParameterList == null || mContractMethodParameterList.size() == 0){
            return 0;
        }

        int index = mContractMethodParameterList.indexOf(parameter);

        if(index == mContractMethodParameterList.size()-1) {
            return 1;
        }

        int chainSize = 0;

        for (int i = index; i< mContractMethodParameterList.size(); i++){
            if(mContractMethodParameterList.get(index).getType().contains(TYPE_STRING)){
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


    private String getStringOffset(String data) {
        return appendNumericPattern(convertToByteCode(data.length()));
    }

    private String getStringLength(String _value) {
        return appendNumericPattern(convertToByteCode(_value.length()/2));
    }

    private String appendStringPattern(String _value) {

        String fullParameter = "";
        fullParameter += getStringLength(_value);

        if(_value.length()<=hashPattern.length()) {
            fullParameter += formNotFullString(_value);
        }else {
            int ost = _value.length() % hashPattern.length();
            fullParameter += _value + hashPattern.substring(0,hashPattern.length()-ost);
        }

        return fullParameter;
    }

    private String appendAddressPattern(String _value){
        return hashPattern.substring(_value.length()) + _value;
    }

    private String formNotFullString(String _value) {
        return _value + hashPattern.substring(_value.length());
    }

    private String appendNumericPattern(String _value){
        return hashPattern.substring(0,hashPattern.length()-_value.length()) + _value;
    }

    private String getByteCodeByUiid(long uiid) {
        return FileStorageManager.getInstance().readByteCodeContract(mContext, uiid);
    }

    public Script createConstructScript(String abiParams){

        byte[] version = Hex.decode("01");
        byte[] gasLimit = Hex.decode("80841e0000000000");
        byte[] gasPrice = Hex.decode("0100000000000000");
        byte[] data = Hex.decode(abiParams);
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
        return new Script(program);
    }

    public Script createMethodScript(String abiParams, String _contractAddress) throws RuntimeException{

        byte[] version = Hex.decode("01");
        byte[] gasLimit = Hex.decode("80841e0000000000");
        byte[] gasPrice = Hex.decode("0100000000000000");
        byte[] data = Hex.decode(abiParams);
        byte[] contractAddress = Hex.decode(_contractAddress);
        byte[] program;

        ScriptChunk versionChunk = new ScriptChunk(OP_PUSHDATA_1,version);
        ScriptChunk gasLimitChunk = new ScriptChunk(OP_PUSHDATA_8,gasLimit);
        ScriptChunk gasPriceChunk = new ScriptChunk(OP_PUSHDATA_8,gasPrice);
        ScriptChunk dataChunk = new ScriptChunk(ScriptOpCodes.OP_PUSHDATA2,data);
        ScriptChunk contactAddressChunk = new ScriptChunk(ScriptOpCodes.OP_PUSHDATA2,contractAddress);
        ScriptChunk opExecChunk = new ScriptChunk(OP_EXEC_ASSIGN, null);
        List<ScriptChunk> chunkList = new ArrayList<>();
        chunkList.add(versionChunk);
        chunkList.add(gasLimitChunk);
        chunkList.add(gasPriceChunk);
        chunkList.add(dataChunk);
        chunkList.add(contactAddressChunk);
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
        return new Script(program);
    }

    public String createTransactionHash(Script script, List<UnspentOutput> unspentOutputs){

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
            throw new RuntimeException("Not enough money");
        }

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
        return Hex.toHexString(bytes);
    }
}
