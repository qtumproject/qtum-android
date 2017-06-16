package com.pixelplex.qtum.ui.fragment.BackupContractsFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;



/**
 * Created by max-v on 6/15/2017.
 */

public class BackupContractsFragmentPresenter extends BaseFragmentPresenterImpl {

    BackupContractsFragmentView mBackupContractsFragmentView;

    public BackupContractsFragmentPresenter(BackupContractsFragmentView backupContractsFragmentView){
        mBackupContractsFragmentView = backupContractsFragmentView;
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

        if(getView().getMainActivity().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            createBackUpFile();
        }

        getView().getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if(requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        backupFile();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBackUpFile.delete();
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
        }
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        if(mBackUpFile.exists()) {
            intentShareFile.setType("application/txt");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+mBackUpFile.getAbsolutePath()));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Qtum Backup File");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Qtum Backup File");
            getView().getMainActivity().startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    private void createBackUpFile(){
        String fileName = "qtum_backup_file.txt";
        String backupData = "Test data";
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
        getView().setUpFile(backUpFileSize);

        mBackUpFile = backupFile;
    }

}
