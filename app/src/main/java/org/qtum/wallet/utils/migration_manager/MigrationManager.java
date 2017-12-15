package org.qtum.wallet.utils.migration_manager;

import android.content.Context;

import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractCreationStatus;
import org.qtum.wallet.model.contract.Token;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MigrationManager {

    private final int migrateVersion_93 = 93;
    private final int migrateVersion_100 = 100    ;
    List<Integer> migrations = new ArrayList<>();

    public MigrationManager() {
        migrations.add(migrateVersion_93);
        migrations.add(migrateVersion_100);
    }

    public int makeMigration(int currentVersion, int migrationVersion, Context context) {
        int newMigrationVersion = migrationVersion;
        for (Integer version : migrations) {
            if (version > migrationVersion) {
                if (!migrate(version, context)) {
                    return newMigrationVersion;
                }
                newMigrationVersion = version;
            }
        }
        return currentVersion;
    }

    private boolean migrate(int version, Context context) {
        switch (version) {
            case migrateVersion_93:
                renameSenderAddress(context);
                clearKeyFile(context);
                return true;
            case migrateVersion_100:
                resetContractCreationStatus(context);
                return true;
            default:
                return false;
        }
    }

    private void clearKeyFile(Context context) {
        File file = new File(context.getFilesDir().getPath() + "/key_storage");
        if (file.exists()) {
            file.delete();
        }
    }

    private void renameSenderAddress(Context context) {
        TinyDB tinyDB = new TinyDB(context);
        List<Contract> contracts = tinyDB.getContractListWithoutToken();
        for (Contract contract : contracts) {
            if (contract.getSenderAddress().equals("Stub!")) {
                contract.setSenderAddress("");
            }
        }
        tinyDB.putContractListWithoutToken(contracts);

        List<Token> tokens = tinyDB.getTokenList();
        for (Token token : tokens) {
            if (token.getSenderAddress().equals("Stub!")) {
                token.setSenderAddress("");
            }
        }
        tinyDB.putTokenList(tokens);
    }

    private void resetContractCreationStatus(Context context){
        TinyDB_94 tinyDB94 = new TinyDB_94(context);

        TinyDB tinyDB = new TinyDB(context);
        List<Token> tokens = new ArrayList<>();
        List<Contract> contracts = new ArrayList<>();

        for (Contract94 contract94 : tinyDB94.getContractListWithoutToken()) {
            if (contract94.isHasBeenCreated()!=null && contract94.isHasBeenCreated()) {
                contracts.add(new Contract(contract94.getContractAddress(),contract94.getUiid(),
                        ContractCreationStatus.Created,contract94.getDate(),contract94.getSenderAddress(),contract94.getContractName()));
            }else{
                contracts.add(new Contract(contract94.getContractAddress(),contract94.getUiid(),
                        ContractCreationStatus.Unconfirmed,contract94.getDate(),contract94.getSenderAddress(),contract94.getContractName()));
            }
        }
        tinyDB.putContractListWithoutToken(contracts);

        for (Token94 token94 : tinyDB94.getTokenList()) {
            if (token94.isHasBeenCreated()!=null && token94.isHasBeenCreated()) {
                tokens.add(new Token(token94.getContractAddress(),token94.getUiid(),ContractCreationStatus.Created,
                        token94.getDate(),token94.getSenderAddress(),token94.getContractName()));
            } else {
                tokens.add(new Token(token94.getContractAddress(),token94.getUiid(),ContractCreationStatus.Unconfirmed,
                        token94.getDate(),token94.getSenderAddress(),token94.getContractName()));
            }
        }
        tinyDB.putTokenList(tokens);
    }
}
