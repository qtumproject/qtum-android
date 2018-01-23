package org.qtum.wallet.ui.fragment.backup_contracts_fragment;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;

import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.backup.Backup;
import org.qtum.wallet.model.backup.ContractJSON;
import org.qtum.wallet.model.backup.TemplateJSON;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.utils.DateCalculator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

public class BackupContractsInteractorImpl implements BackupContractsInteractor {

    Context mContext;

    public BackupContractsInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public Observable<File> createBackUpFile() {
        return rx.Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                TinyDB tinyDB = new TinyDB(mContext);

                List<TemplateJSON> templateJSONList = new ArrayList<>();

                List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
                for (ContractTemplate contractTemplate : contractTemplateList) {
                    String source = FileStorageManager.getInstance().readSourceContract(mContext, contractTemplate.getUuid());
                    String bytecode = FileStorageManager.getInstance().readByteCodeContract(mContext, contractTemplate.getUuid());
                    String abi = FileStorageManager.getInstance().readAbiContract(mContext, contractTemplate.getUuid());
                    TemplateJSON templateJSON = new TemplateJSON(source, bytecode, contractTemplate.getUuid(), DateCalculator.getDateInUtc(contractTemplate.getDate()), abi, contractTemplate.getContractType(), contractTemplate.getName());
                    templateJSONList.add(templateJSON);
                }

                List<ContractJSON> contractList1 = new ArrayList<>();

                List<Contract> contractList = tinyDB.getContractList();
                for (Contract contract : contractList) {
                    String contractTemplateType = tinyDB.getContractTemplateByUiid(contract.getUiid()).getContractType();
                    ContractJSON contract1 = new ContractJSON(contract.getContractName(), contract.getSenderAddress(), contract.getContractAddress(), contractTemplateType, DateCalculator.getDateInUtc(contract.getDate()), contract.getUiid(), contract.isSubscribe());
                    contractList1.add(contract1);
                }

                Backup backup = new Backup(DateCalculator.getDateInUtc(new Date().getTime()), templateJSONList, String.valueOf(android.os.Build.VERSION.SDK_INT), "test", contractList1, "android");
                Gson gson = new Gson();
                String backupData = gson.toJson(backup);

                String fileName = "qtum_backup_file.json";
                File backupFile = new File(Environment.getExternalStorageDirectory(), fileName);

                try {
                    FileOutputStream fOut;
                    fOut = new FileOutputStream(backupFile, true);
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);
                    osw.write(backupData);
                    osw.flush();
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return backupFile;
            }
        });
    }
}
