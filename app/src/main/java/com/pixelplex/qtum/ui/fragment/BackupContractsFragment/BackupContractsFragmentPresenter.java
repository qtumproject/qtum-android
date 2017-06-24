package com.pixelplex.qtum.ui.fragment.BackupContractsFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.backup.Backup;
import com.pixelplex.qtum.model.backup.ContractJSON;
import com.pixelplex.qtum.model.backup.TemplateJSON;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.DateCalculator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by max-v on 6/15/2017.
 */

public class BackupContractsFragmentPresenter extends BaseFragmentPresenterImpl {

    BackupContractsFragmentView mBackupContractsFragmentView;
    Context mContext;
    boolean BACK_UP_FLAG = false;

    public BackupContractsFragmentPresenter(BackupContractsFragmentView backupContractsFragmentView){
        mBackupContractsFragmentView = backupContractsFragmentView;
        mContext = getView().getContext();
    }

    private final int WRITE_EXTERNAL_STORAGE_CODE = 5;

    private File mBackUpFile;

    @Override
    public BackupContractsFragmentView getView() {
        return mBackupContractsFragmentView;
    }

    public void onBackUpClick(){
        checkPermissionAndCreateFile();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();

//        if(getView().getMainActivity().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//            createBackUpFile();
//        }

        getView().getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if(requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        BACK_UP_FLAG = true;
                    }
                }
            }
        });
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        if(BACK_UP_FLAG){
            backupFile();
            BACK_UP_FLAG = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mBackUpFile!=null) {
            mBackUpFile.delete();
        }
        getView().getMainActivity().removePermissionResultListener();
    }

    private void checkPermissionAndCreateFile(){
        if(getView().getMainActivity().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            backupFile();
        } else {
            getView().getMainActivity().loadPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    public void backupFile(){

        if(mBackUpFile==null) {
            createBackUpFile();
        } else {
            chooseShareMethod();
        }
    }

    private void createBackUpFile(){
        getView().setProgressDialog();
        createBackupData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String backupData) {
                        String fileName = "qtum_backup_file.json";
                        File backupFile = new File(Environment.getExternalStorageDirectory(),fileName);

                        try {
                            FileOutputStream fOut = null;
                            fOut = new FileOutputStream(backupFile, true);
                            OutputStreamWriter osw = new OutputStreamWriter(fOut);
                            osw.write(backupData);
                            osw.flush();
                            osw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String backUpFileSize = String.valueOf((int)Math.ceil(backupFile.length()/1024.0)) + " Kb";
                        getView().dismissProgressDialog();
                        getView().setUpFile(backUpFileSize);
                        mBackUpFile = backupFile;

                        chooseShareMethod();
                    }
                });

    }

    public void chooseShareMethod(){
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        if(mBackUpFile.exists()) {
            intentShareFile.setType("application/json");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+mBackUpFile.getAbsolutePath()));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Qtum Backup File");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Qtum Backup File");
            getView().getMainActivity().startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }


    private Observable<String> createBackupData(){
        return rx.Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                TinyDB tinyDB = new TinyDB(mContext);

                List<TemplateJSON> templateJSONList = new ArrayList<>();

                List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
                for(ContractTemplate contractTemplate : contractTemplateList){
                    String source = FileStorageManager.getInstance().readSourceContract(mContext, contractTemplate.getUiid());
                    String bytecode = FileStorageManager.getInstance().readByteCodeContract(mContext,contractTemplate.getUiid());
                    String abi = FileStorageManager.getInstance().readAbiContract(mContext,contractTemplate.getUiid());
                    TemplateJSON templateJSON = new TemplateJSON(source,bytecode,contractTemplate.getUiid(),contractTemplate.getDate(),abi,contractTemplate.getContractType(),contractTemplate.getName());
                    templateJSONList.add(templateJSON);
                }

                List<ContractJSON> contractList1 = new ArrayList<>();

                List<Contract> contractList = tinyDB.getContractList();
                for(Contract contract : contractList){
                    String contractTemplateType = tinyDB.getContractTemplateByUiid(contract.getUiid()).getContractType();
                    ContractJSON contract1 = new ContractJSON(contract.getContractName(),contract.getSenderAddress(),contract.getContractAddress(),contractTemplateType,contract.getDate(),contract.getUiid(),contract.isSubscribe());
                    contractList1.add(contract1);
                }

                Backup backup = new Backup(DateCalculator.getCurrentDate(), templateJSONList,String.valueOf(android.os.Build.VERSION.SDK_INT),"test",contractList1,"android");
                Gson gson = new Gson();
                return gson.toJson(backup);
            }
        });
    }

}
