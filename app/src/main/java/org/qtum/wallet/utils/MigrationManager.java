package org.qtum.wallet.utils;

import android.content.Context;

import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Contract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MigrationManager {

    private final int migrateVersion_09_93 = 93;
    List<Integer> migrations = new ArrayList<>();

    public MigrationManager() {
        migrations.add(migrateVersion_09_93);
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
            case migrateVersion_09_93:
                renameSenderAddress(context);
                clearKeyFile(context);
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
        for (Contract contract : tinyDB.getContractList()) {
            if (contract.getSenderAddress().equals("Stub!")) {
                contract.setSenderAddress("");
            }
        }
    }
}
