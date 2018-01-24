package org.qtum.wallet.utils.migration_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.utils.DateCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TinyDB_104 {

    private final String CONTRACT_LIST = "contract_list";
    private final String TOKEN_LIST = "token_list";
    private final String CONTRACT_TEMPLATE_LIST = "contract_template_list";

    private SharedPreferences preferences;

    public TinyDB_104(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public List<Contract104> getContractList() {

        ArrayList<Contract104> contractArrayList = new ArrayList<>();

        contractArrayList.addAll(getContractListWithoutToken());
        contractArrayList.addAll(getTokenList());

        Collections.sort(contractArrayList, new Comparator<Contract104>() {
            @Override
            public int compare(Contract104 contract, Contract104 t1) {
                if (contract.getDate() == null) {
                    return -1;
                } else if (t1.getDate() == null) {
                    return 1;
                } else {
                    return DateCalculator.equals(contract.getDate(), t1.getDate());
                }
            }
        });

        return contractArrayList;
    }

    public List<Contract104> getContractListWithoutToken() {
        Gson gson = new Gson();

        ArrayList<String> contractInfoStrings = getListString(CONTRACT_LIST);
        ArrayList<Contract104> contractArrayList = new ArrayList<>();

        for (String contractInfoString : contractInfoStrings) {
            Contract104 contract = gson.fromJson(contractInfoString, Contract104.class);
            if (contract != null) {
                contractArrayList.add(contract);
            }
        }

        return contractArrayList;
    }

    public List<Token104> getTokenList() {
        Gson gson = new Gson();

        ArrayList<String> tokenStrings = getListString(TOKEN_LIST);
        ArrayList<Token104> tokenArrayList = new ArrayList<>();
        for (String contractInfoString : tokenStrings) {
            Token104 token = gson.fromJson(contractInfoString, Token104.class);
            if (token != null) {
                tokenArrayList.add(token);
            }
        }

        return tokenArrayList;
    }

    public List<ContractTemplate104> getContractTemplateList() {
        Gson gson = new Gson();

        ArrayList<String> contractTemplateString = getListString(CONTRACT_TEMPLATE_LIST);
        ArrayList<ContractTemplate104> contractTemplateArrayList = new ArrayList<>();

        for (String contractInfoString : contractTemplateString) {
            ContractTemplate104 contractTemplate = gson.fromJson(contractInfoString, ContractTemplate104.class);
            if (contractTemplate != null) {
                contractTemplateArrayList.add(contractTemplate);
            }
        }

        return contractTemplateArrayList;
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public void putTokenList(List<Token104> tokenArrayList) {
        Gson gson = new Gson();
        ArrayList<String> tokenStrings = new ArrayList<>();
        for (Token104 token : tokenArrayList) {
            tokenStrings.add(gson.toJson(token));
        }
        putListString(TOKEN_LIST, tokenStrings);
    }

    public void putContractListWithoutToken(List<Contract104> contractArrayList) {
        Gson gson = new Gson();
        ArrayList<String> contractInfoStrings = new ArrayList<>();
        for (Contract104 contract : contractArrayList) {
            contractInfoStrings.add(gson.toJson(contract));
        }
        putListString(CONTRACT_LIST, contractInfoStrings);
    }

    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

}
