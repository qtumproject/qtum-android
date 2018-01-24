package org.qtum.wallet.utils.migration_manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractCreationStatus;
import org.qtum.wallet.model.contract.Token;

import org.qtum.wallet.utils.DateCalculator;
import org.qtum.wallet.utils.crypto.KeyStoreHelper;

import java.io.File;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

import java.util.List;

import static org.qtum.wallet.utils.crypto.KeyStoreHelper.QTUM_PIN_ALIAS;

public class MigrationManager {

    private final int migrateVersion_93 = 93;

    private final int migrateVersion_100 = 100;

    private final int migrateVersion_104 = 104;

    List<Integer> migrations = new ArrayList<>();

    public MigrationManager() {
        migrations.add(migrateVersion_93);
        migrations.add(migrateVersion_100);
        migrations.add(migrateVersion_104);
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
            case migrateVersion_104:
                resetContractAndTemplateTime(context);
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

    private void resetContractCreationStatus(Context context) {
        TinyDB_94 tinyDB94 = new TinyDB_94(context);

        TinyDB_104 tinyDB = new TinyDB_104(context);
        List<Token104> tokens = new ArrayList<>();
        List<Contract104> contracts = new ArrayList<>();

        for (Contract94 contract94 : tinyDB94.getContractListWithoutToken()) {

            if (contract94.isHasBeenCreated()!=null && contract94.isHasBeenCreated()) {
                contracts.add(new Contract104(contract94.getContractAddress(),contract94.getUiid(),
                        ContractCreationStatus.Created,contract94.getDate(),contract94.getSenderAddress(),contract94.getContractName()));
            }else{
                contracts.add(new Contract104(contract94.getContractAddress(),contract94.getUiid(),
                        ContractCreationStatus.Unconfirmed,contract94.getDate(),contract94.getSenderAddress(),contract94.getContractName()));
            }
        }
        tinyDB.putContractListWithoutToken(contracts);

        for (Token94 token94 : tinyDB94.getTokenList()) {
            if (token94.isHasBeenCreated()!=null && token94.isHasBeenCreated()) {
                tokens.add(new Token104(token94.getContractAddress(),token94.getUiid(),ContractCreationStatus.Created,
                        token94.getDate(),token94.getSenderAddress(),token94.getContractName()));
            } else {
                tokens.add(new Token104(token94.getContractAddress(),token94.getUiid(),ContractCreationStatus.Unconfirmed,
                        token94.getDate(),token94.getSenderAddress(),token94.getContractName()));
            }
        }
        tinyDB.putTokenList(tokens);
    }

    private void resetContractAndTemplateTime(Context context) {
        TinyDB_104 tinyDB104 = new TinyDB_104(context);
        TinyDB tinyDB = new TinyDB(context);

        List<Contract> contracts = new ArrayList<>();
        List<Token> tokens = new ArrayList<>();
        List<ContractTemplate> contractTemplates = new ArrayList<>();

        for(Contract104 contract104 : tinyDB104.getContractListWithoutToken()){
            contracts.add(new Contract(contract104.getContractAddress(),contract104.getUiid(),contract104.getContractName(),contract104.getCreationStatus(),convertDateToLong(contract104.getDate()),contract104.getSenderAddress(),contract104.isSubscribe()));
        }
        tinyDB.putContractListWithoutToken(contracts);

        for(Token104 token104 : tinyDB104.getTokenList()){
            tokens.add(new Token(token104.getContractAddress(),token104.getUiid(),token104.getContractName(),token104.getCreationStatus(),convertDateToLong(token104.getDate()),token104.getSenderAddress(),token104.isSubscribe()));
        }
        tinyDB.putTokenList(tokens);

        for(ContractTemplate104 contractTemplate104 : tinyDB104.getContractTemplateList()){
            contractTemplates.add(new ContractTemplate(contractTemplate104.getName(),convertDateToLong(contractTemplate104.getDate()), contractTemplate104.getContractType(),contractTemplate104.getUuid(),contractTemplate104.isFullContractTemplate(),contractTemplate104.isSelectedABI()));
        }
        tinyDB.putContractTemplate(contractTemplates);
    }

    private Long convertDateToLong(String date){
        return DateCalculator.getLongDate(date)/* - (long) (new GregorianCalendar().getTimeZone()).getRawOffset()*/;
    }

    public static boolean migratePassphrase(Context context) {
        if (!QtumSharedPreference.getInstance().isPassphraseMigrated(context)) {
            String encryptedSaltPassphrase = QtumSharedPreference.getInstance().getSeed(context);
            if(!TextUtils.isEmpty(encryptedSaltPassphrase)) {
                try {
                    KeyStoreHelper.createKeys(context, QTUM_PIN_ALIAS);
                    byte[] bytes = KeyStoreHelper.decryptToBytes(QTUM_PIN_ALIAS, encryptedSaltPassphrase);
                    savePassphraseSalt(context, bytes);
                    QtumSharedPreference.getInstance().setPassphraseMigration(context, true);

                } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | RuntimeException e) {
                    return false;
                }
            } else {
                QtumSharedPreference.getInstance().setPassphraseMigration(context, true);
            }
        }
        return true;
    }

    private static void savePassphraseSalt(Context context, byte[] saltPassphrase) {
        String base64 = Base64.encodeToString(saltPassphrase, Base64.DEFAULT);
        QtumSharedPreference.getInstance().saveSeed(context, base64);
    }
}
