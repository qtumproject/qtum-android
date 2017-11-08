package org.qtum.wallet.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.google.gson.Gson;

import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.model.TransactionHashWithSender;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.utils.sha3.Keccak;
import org.qtum.wallet.utils.sha3.Parameters;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;

public class ContractBuilder {

    private String hashPattern = "0000000000000000000000000000000000000000000000000000000000000000"; //64b

    private final int radix = 16;
    private final String TYPE_INT = "int";
    private final String TYPE_STRING = "string";
    private final String TYPE_ADDRESS = "address";
    private final String TYPE_BOOL = "bool";

    private final String ARRAY_PARAMETER_CHECK_PATTERN = ".*?\\d+\\[\\d*\\]";
    private final String ARRAY_PARAMETER_TYPE = "(.*?\\d+)\\[(\\d*)\\]";

    final int OP_PUSHDATA_1 = 1;
    final int OP_PUSHDATA_4 = 0x04;
    final int OP_PUSHDATA_8 = 8;
    final int OP_EXEC = 193;
    final int OP_EXEC_ASSIGN = 194;
    final int OP_EXEC_SPEND = 195;

    private Context mContext;

    public ContractBuilder() {

    }

    private List<String> getArrayValues(ContractMethodParameter contractMethodParameter) {
        return Arrays.asList(contractMethodParameter.getValue().split("\\s*,\\s*"));
    }

    private boolean parameterIsArray(ContractMethodParameter contractMethodParameter) {
        Pattern p = Pattern.compile(ARRAY_PARAMETER_CHECK_PATTERN);
        Matcher m = p.matcher(contractMethodParameter.getType());
        return m.matches();
    }

    private Pair<String, String> getArrayTypeAndLength(ContractMethodParameter contractMethodParameter) {
        Pattern p = Pattern.compile(ARRAY_PARAMETER_TYPE);
        Matcher m = p.matcher(contractMethodParameter.getType());
        if (m.matches()) {
            return new Pair<>(m.group(1), m.group(2));
        } else {
            throw new TypeNotPresentException(contractMethodParameter.getType(), new Throwable("Can not find type for ABI array parameter"));
        }
    }

    private List<ContractMethodParameter> mContractMethodParameterList;

    public Observable<String> createAbiMethodParams(final String _methodName, final List<ContractMethodParameter> contractMethodParameterList) {
        return rx.Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String methodName = _methodName;
                String parameters = "";
                String abiParams = "";
                mContractMethodParameterList = contractMethodParameterList;
                if (contractMethodParameterList != null && contractMethodParameterList.size() != 0) {
                    for (ContractMethodParameter parameter : contractMethodParameterList) {
                        abiParams += convertParameter(parameter);
                        parameters = parameters + parameter.getType() + ",";
                    }
                    methodName = methodName + "(" + parameters.substring(0, parameters.length() - 1) + ")";
                } else {
                    methodName = methodName + "()";
                }
                Keccak keccak = new Keccak();
                String hashMethod = keccak.getHash(Hex.toHexString((methodName).getBytes()), Parameters.KECCAK_256).substring(0, 8);
                abiParams = hashMethod + abiParams;
                return abiParams;
            }
        });
    }


    long paramsCount;

    public Observable<String> createAbiConstructParams(final List<ContractMethodParameter> contractMethodParameterList, final String uiid, Context context) {
        mContext = context;
        paramsCount = contractMethodParameterList.size();
        currStringOffset = 0;

        return rx.Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String abiParams = "";
                mContractMethodParameterList = contractMethodParameterList;
                if (mContractMethodParameterList != null) {
                    for (ContractMethodParameter parameter : mContractMethodParameterList) {
                        abiParams += convertParameter(parameter);
                    }
                }
                abiParams = getByteCodeByUiid(uiid) + abiParams + appendStringParameters() + appendArrayParameters();
                return abiParams;
            }
        });
    }

    private String convertParameter(ContractMethodParameter parameter) {

        String _value = parameter.getValue();

        if (!parameterIsArray(parameter)) {
            if (parameter.getType().contains(TYPE_INT)) {
                return appendNumericPattern(convertToByteCode(new BigInteger(_value)));
            } else if (parameter.getType().contains(TYPE_STRING)) {
                return getStringOffset(parameter);
            } else if (parameter.getType().contains(TYPE_ADDRESS) && _value.length() == 34) {
                byte[] decode = Base58.decode(_value);
                String toHexString = Hex.toHexString(decode);
                String substring = toHexString.substring(2, 42);
                return appendAddressPattern(substring);
            } else if (parameter.getType().contains(TYPE_ADDRESS)) {
                return getStringOffset(parameter);
            } else if (parameter.getType().contains(TYPE_BOOL)) {
                return appendBoolean(_value);
            }
        } else {
            return getStringOffset(parameter);
        }

        return "";
    }

    private String appendArrayParameters() {
        String stringParams = "";
        for (ContractMethodParameter parameter : mContractMethodParameterList) {
            if (parameterIsArray(parameter)) {
                Pair<String, String> arrayTypeAndLength = getArrayTypeAndLength(parameter);
                List<String> paramsList = getArrayValues(parameter);
                if (paramsList.size() > 0) {
                    String arrayLength = (TextUtils.isEmpty(arrayTypeAndLength.second)) ? String.valueOf(paramsList.size()) : arrayTypeAndLength.second;
                    stringParams += appendNumericPattern(arrayLength);
                    for (String item : paramsList) {
                        stringParams += appendArrayParameter(arrayTypeAndLength.first, item);
                    }
                }
            }
        }
        return stringParams;
    }

    private String appendArrayParameter(String type, String param) {
        if (type.contains(TYPE_INT)) {
            return appendNumericPattern(convertToByteCode(new BigInteger(param)));
        } else if (type.contains(TYPE_BOOL)) {
            return appendBoolean(param);
        } else if (type.contains(TYPE_ADDRESS)) {
            return appendAddressPattern(param);
        }
        return "";
    }

    private String appendStringParameters() {
        String stringParams = "";
        for (ContractMethodParameter parameter : mContractMethodParameterList) {
            if (parameter.getType().contains(TYPE_STRING)) {
                stringParams += appendStringPattern(convertToByteCode(parameter.getValue()));
            }
        }
        return stringParams;
    }

    private String appendBoolean(String parameter) {
        return Boolean.valueOf(parameter) ? appendNumericPattern("1") : appendNumericPattern("0");
    }

    private int getStringsChainSize(ContractMethodParameter parameter) {

        if (mContractMethodParameterList == null || mContractMethodParameterList.size() == 0) {
            return 0;
        }

        int index = mContractMethodParameterList.indexOf(parameter);

        if (index == mContractMethodParameterList.size() - 1) {
            return 1;
        }

        int chainSize = 0;

        for (int i = index; i < mContractMethodParameterList.size(); i++) {
            if (mContractMethodParameterList.get(index).getType().contains(TYPE_STRING)) {
                chainSize++;
            }
        }

        return chainSize;
    }

    private String convertToByteCode(long _value) {
        return Long.toString(_value, radix);
    }

    private String convertToByteCode(BigInteger _value) {
        return _value.toString(radix);
    }

    private static String convertToByteCode(String _value) {
        char[] chars = _value.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }
        return hex.toString();
    }

    private String getStringOffset(String data) {
        return appendNumericPattern(convertToByteCode(data.length()));
    }

    long currStringOffset = 0;

    private String getStringOffset(ContractMethodParameter parameter) {
        long currOffset = ((paramsCount + currStringOffset) * 32);
        currStringOffset = getStringHash(parameter.getValue()).length() / hashPattern.length() + 1/*string length section*/;
        return appendNumericPattern(convertToByteCode(currOffset));
    }

    private String getStringLength(String _value) {
        return appendNumericPattern(convertToByteCode(_value.length()));
    }

    private String getStringHash(String _value) {
        if (_value.length() <= hashPattern.length()) {
            return formNotFullString(_value);
        } else {
            int ost = _value.length() % hashPattern.length();
            return _value + hashPattern.substring(0, hashPattern.length() - ost);
        }
    }

    private String appendStringPattern(String _value) {

        String fullParameter = "";
        fullParameter += getStringLength(_value);

        if (_value.length() <= hashPattern.length()) {
            fullParameter += formNotFullString(_value);
        } else {
            int ost = _value.length() % hashPattern.length();
            fullParameter += _value + hashPattern.substring(0, hashPattern.length() - ost);
        }

        return fullParameter;
    }

    private String appendAddressPattern(String _value) {
        return hashPattern.substring(_value.length()) + _value;
    }

    private String formNotFullString(String _value) {
        return _value + hashPattern.substring(_value.length());
    }

    private String appendNumericPattern(String _value) {
        return hashPattern.substring(0, hashPattern.length() - _value.length()) + _value;
    }

    private String getByteCodeByUiid(String uiid) {
        return FileStorageManager.getInstance().readByteCodeContract(mContext, uiid);
    }

    public Script createConstructScript(String abiParams, int gasLimitInt, int gasPriceInt) {

        byte[] version = Hex.decode("04000000");

        byte[] arrayGasLimit = org.spongycastle.util.Arrays.reverse((new BigInteger(String.valueOf(gasLimitInt))).toByteArray());
        byte[] gasLimit = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(arrayGasLimit, 0, gasLimit, 0, arrayGasLimit.length);

        byte[] arrayGasPrice = org.spongycastle.util.Arrays.reverse((new BigInteger(String.valueOf(gasPriceInt))).toByteArray());
        byte[] gasPrice = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(arrayGasPrice, 0, gasPrice, 0, arrayGasPrice.length);

        byte[] data = Hex.decode(abiParams);
        byte[] program;

        ScriptChunk versionChunk = new ScriptChunk(OP_PUSHDATA_4, version);
        ScriptChunk gasLimitChunk = new ScriptChunk(OP_PUSHDATA_8, gasLimit);
        ScriptChunk gasPriceChunk = new ScriptChunk(OP_PUSHDATA_8, gasPrice);
        ScriptChunk dataChunk = new ScriptChunk(ScriptOpCodes.OP_PUSHDATA2, data);
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

    public Script createMethodScript(String abiParams, int gasLimitInt, int gasPriceInt, String _contractAddress) throws RuntimeException {

        byte[] version = Hex.decode("04000000");

        byte[] arrayGasLimit = org.spongycastle.util.Arrays.reverse((new BigInteger(String.valueOf(gasLimitInt))).toByteArray());
        byte[] gasLimit = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(arrayGasLimit, 0, gasLimit, 0, arrayGasLimit.length);

        byte[] arrayGasPrice = org.spongycastle.util.Arrays.reverse((new BigInteger(String.valueOf(gasPriceInt))).toByteArray());
        byte[] gasPrice = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(arrayGasPrice, 0, gasPrice, 0, arrayGasPrice.length);

        byte[] data = Hex.decode(abiParams);
        byte[] contractAddress = Hex.decode(_contractAddress);
        byte[] program;

        ScriptChunk versionChunk = new ScriptChunk(OP_PUSHDATA_4, version);
        ScriptChunk gasLimitChunk = new ScriptChunk(OP_PUSHDATA_8, gasLimit);
        ScriptChunk gasPriceChunk = new ScriptChunk(OP_PUSHDATA_8, gasPrice);
        ScriptChunk dataChunk = new ScriptChunk(ScriptOpCodes.OP_PUSHDATA2, data);
        ScriptChunk contactAddressChunk = new ScriptChunk(ScriptOpCodes.OP_PUSHDATA2, contractAddress);
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

    public String createTransactionHash(Script script, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, BigDecimal feePerKb, String feeString, Context context) {

        Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
        transaction.addOutput(Coin.ZERO, script);
        BigDecimal fee = new BigDecimal(feeString);
        BigDecimal gasFee = (new BigDecimal(gasLimit)).multiply(new BigDecimal(gasPrice)).divide(new BigDecimal(100000000), MathContext.DECIMAL128);
        BigDecimal totalFee = fee.add(gasFee);
        BigDecimal amountFromOutput = new BigDecimal("0.0");
        BigDecimal overFlow = new BigDecimal("0.0");
        for (UnspentOutput unspentOutput : unspentOutputs) {
            overFlow = overFlow.add(unspentOutput.getAmount());
            if (overFlow.doubleValue() >= totalFee.doubleValue()) {
                break;
            }
        }
        if (overFlow.doubleValue() < totalFee.doubleValue()) {
            throw new RuntimeException("You have insufficient funds for this transaction");
        }
        BigDecimal delivery = overFlow.subtract(totalFee);


        BigDecimal bitcoin = new BigDecimal(100000000);

        ECKey myKey = KeyStorage.getInstance().getCurrentKey();

        if (delivery.doubleValue() != 0.0) {
            transaction.addOutput(Coin.valueOf((long) (delivery.multiply(bitcoin).doubleValue())), myKey.toAddress(CurrentNetParams.getNetParams()));
        }


        for (UnspentOutput unspentOutput : unspentOutputs) {
            if (unspentOutput.getAmount().doubleValue() != 0.0)
                for (DeterministicKey deterministicKey : KeyStorage.getInstance().getKeyList()) {
                    if (deterministicKey.toAddress(CurrentNetParams.getNetParams()).toString().equals(unspentOutput.getAddress())) {
                        Sha256Hash sha256Hash = new Sha256Hash(Utils.parseAsHexOrBase58(unspentOutput.getTxHash()));
                        TransactionOutPoint outPoint = new TransactionOutPoint(CurrentNetParams.getNetParams(), unspentOutput.getVout(), sha256Hash);
                        Script script2 = new Script(Utils.parseAsHexOrBase58(unspentOutput.getTxoutScriptPubKey()));
                        transaction.addSignedInput(outPoint, script2, deterministicKey, Transaction.SigHash.ALL, true);
                        amountFromOutput = amountFromOutput.add(unspentOutput.getAmount());
                        break;
                    }
                }
            if (amountFromOutput.doubleValue() >= totalFee.doubleValue()) {
                break;
            }
        }

        transaction.getConfidence().setSource(TransactionConfidence.Source.SELF);
        transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);

        byte[] bytes = transaction.unsafeBitcoinSerialize();

        int txSizeInkB = (int) Math.ceil(bytes.length / 1024.);
        BigDecimal minimumFee = (feePerKb.multiply(new BigDecimal(txSizeInkB)));
        if (minimumFee.doubleValue() > fee.doubleValue()) {
            throw new RuntimeException(context.getString(R.string.insufficient_fee_lease_use_minimum_of) + " " + minimumFee.toString() + " QTUM");
        }

        return Hex.toHexString(bytes);
    }

    public TransactionHashWithSender createTransactionHashForCreateContract(Script script, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, BigDecimal feePerKb, String feeString, Context context) {

        Transaction transaction = new Transaction(CurrentNetParams.getNetParams());
        transaction.addOutput(Coin.ZERO, script);
        BigDecimal fee = new BigDecimal(feeString);
        BigDecimal gasFee = (new BigDecimal(gasLimit)).multiply(new BigDecimal(gasPrice)).divide(new BigDecimal(100000000), MathContext.DECIMAL128);
        BigDecimal totalFee = fee.add(gasFee);
        BigDecimal amountFromOutput = new BigDecimal("0.0");
        BigDecimal overFlow = new BigDecimal("0.0");
        for (UnspentOutput unspentOutput : unspentOutputs) {
            overFlow = overFlow.add(unspentOutput.getAmount());
            if (overFlow.doubleValue() >= totalFee.doubleValue()) {
                break;
            }
        }
        if (overFlow.doubleValue() < totalFee.doubleValue()) {
            throw new RuntimeException("You have insufficient funds for this transaction");
        }
        BigDecimal delivery = overFlow.subtract(totalFee);


        BigDecimal bitcoin = new BigDecimal(100000000);

        ECKey myKey = KeyStorage.getInstance().getCurrentKey();

        if (delivery.doubleValue() != 0.0) {
            transaction.addOutput(Coin.valueOf((long) (delivery.multiply(bitcoin).doubleValue())), myKey.toAddress(CurrentNetParams.getNetParams()));
        }


        for (UnspentOutput unspentOutput : unspentOutputs) {
            if (unspentOutput.getAmount().doubleValue() != 0.0)
                for (DeterministicKey deterministicKey : KeyStorage.getInstance().getKeyList()) {
                    if (deterministicKey.toAddress(CurrentNetParams.getNetParams()).toString().equals(unspentOutput.getAddress())) {
                        Sha256Hash sha256Hash = new Sha256Hash(Utils.parseAsHexOrBase58(unspentOutput.getTxHash()));
                        TransactionOutPoint outPoint = new TransactionOutPoint(CurrentNetParams.getNetParams(), unspentOutput.getVout(), sha256Hash);
                        Script script2 = new Script(Utils.parseAsHexOrBase58(unspentOutput.getTxoutScriptPubKey()));
                        transaction.addSignedInput(outPoint, script2, deterministicKey, Transaction.SigHash.ALL, true);
                        amountFromOutput = amountFromOutput.add(unspentOutput.getAmount());
                        break;
                    }
                }
            if (amountFromOutput.doubleValue() >= totalFee.doubleValue()) {
                break;
            }
        }

        transaction.getConfidence().setSource(TransactionConfidence.Source.SELF);
        transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);

        byte[] bytes = transaction.unsafeBitcoinSerialize();

        int txSizeInkB = (int) Math.ceil(bytes.length / 1024.);
        BigDecimal minimumFee = (feePerKb.multiply(new BigDecimal(txSizeInkB)));
        if (minimumFee.doubleValue() > fee.doubleValue()) {
            throw new RuntimeException(context.getString(R.string.insufficient_fee_lease_use_minimum_of) + " " + minimumFee.toString() + " QTUM");
        }

        return new TransactionHashWithSender(Hex.toHexString(bytes),transaction.getInputs().get(0).getFromAddress().toString());
    }


    private static String FUNCTION_TYPE = "function";
    private static String TYPE = "type";

    public static boolean checkForValidityQRC20(String abiCode) {

        List<ContractMethod> standardInterface = initStandardInterface();

        JSONArray array;
        List<ContractMethod> contractMethods = new ArrayList<>();
        try {
            array = new JSONArray(abiCode);

            for (int i = 0; i < array.length(); i++) {
                JSONObject jb = array.getJSONObject(i);
                if (FUNCTION_TYPE.equals(jb.getString(TYPE))) {
                    Gson gson = new Gson();
                    contractMethods.add(gson.fromJson(jb.toString(), ContractMethod.class));
                }
            }

            boolean validityFlag = true;
            for (ContractMethod contractMethodStandard : standardInterface) {
                for (ContractMethod contractMethod : contractMethods) {
                    if (contractMethod.getName().equals(contractMethodStandard.getName()) && contractMethod.getType().contains(contractMethodStandard.getType()) && contractMethod.isConstant() == contractMethodStandard.isConstant()) {

                        if (contractMethodStandard.getInputParams() != null) {
                            if (contractMethod.getInputParams() == null) {
                                return false;
                            }
                            for (ContractMethodParameter contractMethodParameterStandard : contractMethodStandard.getInputParams()) {
                                Iterator<ContractMethodParameter> contractMethodParameterIterator = contractMethod.getInputParams().iterator();
                                while (contractMethodParameterIterator.hasNext()) {
                                    ContractMethodParameter contractMethodParameter = contractMethodParameterIterator.next();
                                    if (contractMethodParameter.getType().contains(contractMethodParameterStandard.getType())) {
                                        validityFlag = true;
                                        contractMethodParameterIterator.remove();
                                        break;
                                    }
                                    validityFlag = false;
                                }
                                if (!validityFlag) return false;
                            }
                            if (contractMethod.getInputParams().size() != 0) {
                                return false;
                            }
                        }
                        if (contractMethodStandard.getOutputParams() != null) {
                            if (contractMethod.getOutputParams() == null) {
                                return false;
                            }
                            for (ContractMethodParameter contractMethodParameterStandard : contractMethodStandard.getOutputParams()) {
                                Iterator<ContractMethodParameter> contractMethodParameterIterator = contractMethod.getOutputParams().iterator();
                                while (contractMethodParameterIterator.hasNext()) {
                                    ContractMethodParameter contractMethodParameter = contractMethodParameterIterator.next();
                                    if (contractMethodParameter.getType().contains(contractMethodParameterStandard.getType())) {
                                        validityFlag = true;
                                        contractMethodParameterIterator.remove();
                                        break;
                                    }
                                    validityFlag = false;
                                }
                                if (!validityFlag) return false;
                            }
                            if (contractMethod.getOutputParams().size() != 0) {
                                return false;
                            }
                        }
                        validityFlag = true;
                        break;
                    }
                    validityFlag = false;
                }
                if (!validityFlag) return false;
            }
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static List<ContractMethod> initStandardInterface() {

        List<ContractMethod> standardInterface = new ArrayList<>();

        List<ContractMethodParameter> totalSupplyOutputParams = new ArrayList<>();
        totalSupplyOutputParams.add(new ContractMethodParameter("totalSupply", "uint"));
        ContractMethod totalSupply = new ContractMethod(true, "function", null, "totalSupply", totalSupplyOutputParams);

        List<ContractMethodParameter> balanceOfInputParams = new ArrayList<>();
        balanceOfInputParams.add(new ContractMethodParameter("_owner", "address"));
        List<ContractMethodParameter> balanceOfOutputParams = new ArrayList<>();
        balanceOfOutputParams.add(new ContractMethodParameter("balance", "uint"));
        ContractMethod balanceOfSupply = new ContractMethod(true, "function", balanceOfInputParams, "balanceOf", balanceOfOutputParams);

        List<ContractMethodParameter> transferInputParams = new ArrayList<>();
        transferInputParams.add(new ContractMethodParameter("_to", "address"));
        transferInputParams.add(new ContractMethodParameter("_value", "uint"));
        List<ContractMethodParameter> transferOutputParams = new ArrayList<>();
        transferOutputParams.add(new ContractMethodParameter("success", "bool"));
        ContractMethod transfer = new ContractMethod(false, "function", transferInputParams, "transfer", transferOutputParams);

        List<ContractMethodParameter> transferFromInputParams = new ArrayList<>();
        transferFromInputParams.add(new ContractMethodParameter("_from", "address"));
        transferFromInputParams.add(new ContractMethodParameter("_to", "address"));
        transferFromInputParams.add(new ContractMethodParameter("_value", "uint"));
        List<ContractMethodParameter> transferFromOutputParams = new ArrayList<>();
        transferFromOutputParams.add(new ContractMethodParameter("success", "bool"));
        ContractMethod transferFrom = new ContractMethod(false, "function", transferFromInputParams, "transferFrom", transferFromOutputParams);

        List<ContractMethodParameter> approveInputParams = new ArrayList<>();
        approveInputParams.add(new ContractMethodParameter("_spender", "address"));
        approveInputParams.add(new ContractMethodParameter("_value", "uint"));
        List<ContractMethodParameter> approveOutputParams = new ArrayList<>();
        approveOutputParams.add(new ContractMethodParameter("success", "bool"));
        ContractMethod approve = new ContractMethod(false, "function", approveInputParams, "approve", approveOutputParams);

        List<ContractMethodParameter> allowanceInputParams = new ArrayList<>();
        allowanceInputParams.add(new ContractMethodParameter("_owner", "address"));
        allowanceInputParams.add(new ContractMethodParameter("_spender", "address"));
        List<ContractMethodParameter> allowanceOutputParams = new ArrayList<>();
        allowanceOutputParams.add(new ContractMethodParameter("remaining", "uint"));
        ContractMethod allowance = new ContractMethod(true, "function", allowanceInputParams, "allowance", allowanceOutputParams);

        standardInterface.add(totalSupply);
        standardInterface.add(balanceOfSupply);
        standardInterface.add(transfer);
        standardInterface.add(transferFrom);
        standardInterface.add(approve);
        standardInterface.add(allowance);

        return standardInterface;
    }

    public static String generateContractAddress(String txHash) {
        char[] ca = txHash.toCharArray();
        StringBuilder sb = new StringBuilder(txHash.length());
        for (int i = 0; i < txHash.length(); i += 2) {
            sb.insert(0, ca, i, 2);
        }

        String reverse_tx_hash = sb.toString();
        reverse_tx_hash = reverse_tx_hash.concat("00000000");


        byte[] test5 = Hex.decode(reverse_tx_hash);

        SHA256Digest sha256Digest = new SHA256Digest();
        sha256Digest.update(test5, 0, test5.length);
        byte[] out = new byte[sha256Digest.getDigestSize()];
        sha256Digest.doFinal(out, 0);

        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(out, 0, out.length);
        byte[] out2 = new byte[ripemd160Digest.getDigestSize()];
        ripemd160Digest.doFinal(out2, 0);

        return Hex.toHexString(out2);
    }

    public static String getShortBigNumberRepresentation(String value) {
        BigDecimal number = new BigDecimal(value);
        NumberFormat formatter = new DecimalFormat("0.##E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        return formatter.format(number);
    }

    public static String getShortBigNumberRepresentation(String value, int maxNumbers) {
        if(value.length() > maxNumbers) {
            BigDecimal number = new BigDecimal(value);
            NumberFormat formatter = new DecimalFormat("0.##E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
            return formatter.format(number);
        } else {
            return value;
        }
    }

}
