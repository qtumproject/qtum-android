package org.qtum.wallet.utils.migration_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.utils.DateCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TinyDB_94 {

    private final String CONTRACT_LIST = "contract_list";
    private final String TOKEN_LIST = "token_list";

    private SharedPreferences preferences;

    public TinyDB_94(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    public List<Contract94> getContractList() {

        ArrayList<Contract94> contractArrayList = new ArrayList<>();

        contractArrayList.addAll(getContractListWithoutToken());
        contractArrayList.addAll(getTokenList());

        Collections.sort(contractArrayList, new Comparator<Contract94>() {
            @Override
            public int compare(Contract94 contract, Contract94 t1) {
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

    public List<Contract94> getContractListWithoutToken() {
        Gson gson = new Gson();

        ArrayList<String> contractInfoStrings = getListString(CONTRACT_LIST);
        ArrayList<Contract94> contractArrayList = new ArrayList<>();

        for (String contractInfoString : contractInfoStrings) {
            Contract94 contract = gson.fromJson(contractInfoString, Contract94.class);
            if (contract != null) {
                contractArrayList.add(contract);
            }
        }

        return contractArrayList;
    }

    public List<Token94> getTokenList() {
        Gson gson = new Gson();

        ArrayList<String> tokenStrings = getListString(TOKEN_LIST);
        ArrayList<Token94> tokenArrayList = new ArrayList<>();
        for (String contractInfoString : tokenStrings) {
            Token94 token = gson.fromJson(contractInfoString, Token94.class);
            if (token != null) {
                tokenArrayList.add(token);
            }
        }

        return tokenArrayList;
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

}
