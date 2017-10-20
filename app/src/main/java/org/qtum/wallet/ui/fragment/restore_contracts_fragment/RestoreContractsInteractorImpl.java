package org.qtum.wallet.ui.fragment.restore_contracts_fragment;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.backup.Backup;
import org.qtum.wallet.model.backup.ContractJSON;
import org.qtum.wallet.model.backup.TemplateJSON;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public class RestoreContractsInteractorImpl implements RestoreContractsInteractor {

    private WeakReference<Context> mContext;

    public RestoreContractsInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public Backup getBackupFromFile(File restoreFile) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(readFile(restoreFile), Backup.class);
    }

    private String readFile(File file) {
        FileReader inputFile = null;
        String data = "";
        try {
            inputFile = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(inputFile);

            String line;
            while ((line = bufferReader.readLine()) != null) {
                data += line;
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public List<ContractTemplate> getContractTemplates() {
        TinyDB tinyDB = new TinyDB(mContext.get());
        return tinyDB.getContractTemplateList();
    }

    @Override
    public ContractTemplate importTemplate(TemplateJSON templateJSON, List<ContractTemplate> templates) {
        return FileStorageManager.getInstance().importTemplate(mContext.get(), templateJSON, templates);
    }

    @Override
    public void putListWithoutToken(List<Contract> contractList) {
        TinyDB tinyDB = new TinyDB(mContext.get());
        List<Contract> tmpContractList = tinyDB.getContractListWithoutToken();
        tmpContractList.addAll(contractList);
        tinyDB.putContractListWithoutToken(tmpContractList);
    }

    private List<Token> filterTokenListForDuplicate(List<Token> newTokens, List<Token> currentTokens) {
        for (Token newToken : newTokens) {
            if (!isTokenExist(newToken, currentTokens)) {
                currentTokens.add(newToken);
            }
        }
        return currentTokens;
    }

    private boolean isTokenExist(Token token, List<Token> oldTokens) {
        for (Token t : oldTokens) {
            if (!TextUtils.isEmpty(t.getContractAddress()) && t.getContractAddress().equals(token.getContractAddress())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void putTokenList(List<Token> tokenList) {
        TinyDB tinyDB = new TinyDB(mContext.get());
        List<Token> tmpTokenList = tinyDB.getTokenList();
        tmpTokenList = filterTokenListForDuplicate(tokenList, tmpTokenList);
        tinyDB.putTokenList(tmpTokenList);
    }

    @Override
    public boolean validateContractCreationAddress(ContractJSON contractJSON, List<TemplateJSON> templates) {
        for (TemplateJSON t : templates) {
            if (t.getUuid().equals(contractJSON.getTemplate())) {
                if (TextUtils.isEmpty(contractJSON.getContractCreationAddres()) && t.isFull()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean getTemplateValidity(TemplateJSON templateJSON) {
        return templateJSON.getValidity();
    }

    @Override
    public boolean getContractValidity(ContractJSON contractJSON) {
        return contractJSON.getValidity();
    }
}
